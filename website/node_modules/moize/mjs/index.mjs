import memoize from 'micro-memoize/mjs/index.mjs';
import { deepEqual, shallowEqual, sameValueZeroEqual } from 'fast-equals/dist/fast-equals.mjs';

function _extends() {
  _extends = Object.assign ? Object.assign.bind() : function (target) {
    for (var i = 1; i < arguments.length; i++) {
      var source = arguments[i];
      for (var key in source) {
        if (Object.prototype.hasOwnProperty.call(source, key)) {
          target[key] = source[key];
        }
      }
    }
    return target;
  };
  return _extends.apply(this, arguments);
}
function _objectWithoutPropertiesLoose(source, excluded) {
  if (source == null) return {};
  var target = {};
  var sourceKeys = Object.keys(source);
  var key, i;
  for (i = 0; i < sourceKeys.length; i++) {
    key = sourceKeys[i];
    if (excluded.indexOf(key) >= 0) continue;
    target[key] = source[key];
  }
  return target;
}

/**
 * @private
 *
 * @constant DEFAULT_OPTIONS
 */
var DEFAULT_OPTIONS = {
  isDeepEqual: false,
  isPromise: false,
  isReact: false,
  isSerialized: false,
  isShallowEqual: false,
  matchesArg: undefined,
  matchesKey: undefined,
  maxAge: undefined,
  maxArgs: undefined,
  maxSize: 1,
  onExpire: undefined,
  profileName: undefined,
  serializer: undefined,
  updateCacheForKey: undefined,
  transformArgs: undefined,
  updateExpire: false
};

/**
 * @private
 *
 * @description
 * method to combine functions and return a single function that fires them all
 *
 * @param functions the functions to compose
 * @returns the composed function
 */
function combine() {
  for (var _len = arguments.length, functions = new Array(_len), _key = 0; _key < _len; _key++) {
    functions[_key] = arguments[_key];
  }
  return functions.reduce(function (f, g) {
    if (typeof f === 'function') {
      return typeof g === 'function' ? function () {
        f.apply(this, arguments);
        g.apply(this, arguments);
      } : f;
    }
    if (typeof g === 'function') {
      return g;
    }
  });
}

/**
 * @private
 *
 * @description
 * method to compose functions and return a single function
 *
 * @param functions the functions to compose
 * @returns the composed function
 */
function compose() {
  for (var _len2 = arguments.length, functions = new Array(_len2), _key2 = 0; _key2 < _len2; _key2++) {
    functions[_key2] = arguments[_key2];
  }
  return functions.reduce(function (f, g) {
    if (typeof f === 'function') {
      return typeof g === 'function' ? function () {
        return f(g.apply(this, arguments));
      } : f;
    }
    if (typeof g === 'function') {
      return g;
    }
  });
}

/**
 * @private
 *
 * @description
 * find the index of the expiration based on the key
 *
 * @param expirations the list of expirations
 * @param key the key to match
 * @returns the index of the expiration
 */
function findExpirationIndex(expirations, key) {
  for (var index = 0; index < expirations.length; index++) {
    if (expirations[index].key === key) {
      return index;
    }
  }
  return -1;
}

/**
 * @private
 *
 * @description
 * create function that finds the index of the key in the list of cache keys
 *
 * @param isEqual the function to test individual argument equality
 * @param isMatchingKey the function to test full key equality
 * @returns the function that finds the index of the key
 */
function createFindKeyIndex(isEqual, isMatchingKey) {
  var areKeysEqual = typeof isMatchingKey === 'function' ? isMatchingKey : function (cacheKey, key) {
    for (var index = 0; index < key.length; index++) {
      if (!isEqual(cacheKey[index], key[index])) {
        return false;
      }
    }
    return true;
  };
  return function (keys, key) {
    for (var keysIndex = 0; keysIndex < keys.length; keysIndex++) {
      if (keys[keysIndex].length === key.length && areKeysEqual(keys[keysIndex], key)) {
        return keysIndex;
      }
    }
    return -1;
  };
}
/**
 * @private
 *
 * @description
 * merge two options objects, combining or composing functions as necessary
 *
 * @param originalOptions the options that already exist on the method
 * @param newOptions the new options to merge
 * @returns the merged options
 */
function mergeOptions(originalOptions, newOptions) {
  if (!newOptions || newOptions === DEFAULT_OPTIONS) {
    return originalOptions;
  }
  return _extends({}, originalOptions, newOptions, {
    onCacheAdd: combine(originalOptions.onCacheAdd, newOptions.onCacheAdd),
    onCacheChange: combine(originalOptions.onCacheChange, newOptions.onCacheChange),
    onCacheHit: combine(originalOptions.onCacheHit, newOptions.onCacheHit),
    transformArgs: compose(originalOptions.transformArgs, newOptions.transformArgs)
  });
}
function isMoized(fn) {
  return typeof fn === 'function' && fn.isMoized;
}
function setName(fn, originalFunctionName, profileName) {
  try {
    var name = profileName || originalFunctionName || 'anonymous';
    Object.defineProperty(fn, 'name', {
      configurable: true,
      enumerable: false,
      value: "moized(" + name + ")",
      writable: true
    });
  } catch (_unused) {
    // For engines where `function.name` is not configurable, do nothing.
  }
}

/**
 * @private
 *
 * @description
 * clear an active expiration and remove it from the list if applicable
 *
 * @param expirations the list of expirations
 * @param key the key to clear
 * @param shouldRemove should the expiration be removed from the list
 */
function clearExpiration(expirations, key, shouldRemove) {
  var expirationIndex = findExpirationIndex(expirations, key);
  if (expirationIndex !== -1) {
    clearTimeout(expirations[expirationIndex].timeoutId);
    if (shouldRemove) {
      expirations.splice(expirationIndex, 1);
    }
  }
}

/**
 * @private
 *
 * @description
 * Create the timeout for the given expiration method. If the ability to `unref`
 * exists, then apply it to avoid process locks in NodeJS.
 *
 * @param expirationMethod the method to fire upon expiration
 * @param maxAge the time to expire after
 * @returns the timeout ID
 */
function createTimeout(expirationMethod, maxAge) {
  var timeoutId = setTimeout(expirationMethod, maxAge);
  if (typeof timeoutId.unref === 'function') {
    timeoutId.unref();
  }
  return timeoutId;
}

/**
 * @private
 *
 * @description
 * create a function that, when an item is added to the cache, adds an expiration for it
 *
 * @param expirations the mutable expirations array
 * @param options the options passed on initialization
 * @param isEqual the function to check argument equality
 * @param isMatchingKey the function to check complete key equality
 * @returns the onCacheAdd function to handle expirations
 */
function createOnCacheAddSetExpiration(expirations, options, isEqual, isMatchingKey) {
  var maxAge = options.maxAge;
  return function onCacheAdd(cache, moizedOptions, moized) {
    var key = cache.keys[0];
    if (findExpirationIndex(expirations, key) === -1) {
      var expirationMethod = function expirationMethod() {
        var findKeyIndex = createFindKeyIndex(isEqual, isMatchingKey);
        var keyIndex = findKeyIndex(cache.keys, key);
        var value = cache.values[keyIndex];
        if (~keyIndex) {
          cache.keys.splice(keyIndex, 1);
          cache.values.splice(keyIndex, 1);
          if (typeof options.onCacheChange === 'function') {
            options.onCacheChange(cache, moizedOptions, moized);
          }
        }
        clearExpiration(expirations, key, true);
        if (typeof options.onExpire === 'function' && options.onExpire(key) === false) {
          cache.keys.unshift(key);
          cache.values.unshift(value);
          onCacheAdd(cache, moizedOptions, moized);
          if (typeof options.onCacheChange === 'function') {
            options.onCacheChange(cache, moizedOptions, moized);
          }
        }
      };
      expirations.push({
        expirationMethod: expirationMethod,
        key: key,
        timeoutId: createTimeout(expirationMethod, maxAge)
      });
    }
  };
}

/**
 * @private
 *
 * @description
 * creates a function that, when a cache item is hit, reset the expiration
 *
 * @param expirations the mutable expirations array
 * @param options the options passed on initialization
 * @returns the onCacheAdd function to handle expirations
 */
function createOnCacheHitResetExpiration(expirations, options) {
  return function onCacheHit(cache) {
    var key = cache.keys[0];
    var expirationIndex = findExpirationIndex(expirations, key);
    if (~expirationIndex) {
      clearExpiration(expirations, key, false);
      expirations[expirationIndex].timeoutId = createTimeout(expirations[expirationIndex].expirationMethod, options.maxAge);
    }
  };
}

/**
 * @private
 *
 * @description
 * get the micro-memoize options specific to the maxAge option
 *
 * @param expirations the expirations for the memoized function
 * @param options the options passed to the moizer
 * @param isEqual the function to test equality of the key on a per-argument basis
 * @param isMatchingKey the function to test equality of the whole key
 * @returns the object of options based on the entries passed
 */
function getMaxAgeOptions(expirations, options, isEqual, isMatchingKey) {
  var onCacheAdd = typeof options.maxAge === 'number' && isFinite(options.maxAge) ? createOnCacheAddSetExpiration(expirations, options, isEqual, isMatchingKey) : undefined;
  return {
    onCacheAdd: onCacheAdd,
    onCacheHit: onCacheAdd && options.updateExpire ? createOnCacheHitResetExpiration(expirations, options) : undefined
  };
}

var statsCache = {
  anonymousProfileNameCounter: 1,
  isCollectingStats: false,
  profiles: {}
};
var hasWarningDisplayed = false;
function clearStats(profileName) {
  if (profileName) {
    delete statsCache.profiles[profileName];
  } else {
    statsCache.profiles = {};
  }
}

/**
 * @private
 *
 * @description
 * activate stats collection
 *
 * @param isCollectingStats should stats be collected
 */
function collectStats(isCollectingStats) {
  if (isCollectingStats === void 0) {
    isCollectingStats = true;
  }
  statsCache.isCollectingStats = isCollectingStats;
}

/**
 * @private
 *
 * @description
 * create a function that increments the number of calls for the specific profile
 */
function createOnCacheAddIncrementCalls(options) {
  var profileName = options.profileName;
  return function () {
    if (profileName && !statsCache.profiles[profileName]) {
      statsCache.profiles[profileName] = {
        calls: 0,
        hits: 0
      };
    }
    statsCache.profiles[profileName].calls++;
  };
}

/**
 * @private
 *
 * @description
 * create a function that increments the number of calls and cache hits for the specific profile
 */
function createOnCacheHitIncrementCallsAndHits(options) {
  return function () {
    var profiles = statsCache.profiles;
    var profileName = options.profileName;
    if (!profiles[profileName]) {
      profiles[profileName] = {
        calls: 0,
        hits: 0
      };
    }
    profiles[profileName].calls++;
    profiles[profileName].hits++;
  };
}

/**
 * @private
 *
 * @description
 * get the profileName for the function when one is not provided
 *
 * @param fn the function to be memoized
 * @returns the derived profileName for the function
 */
function getDefaultProfileName(fn) {
  return fn.displayName || fn.name || "Anonymous " + statsCache.anonymousProfileNameCounter++;
}

/**
 * @private
 *
 * @description
 * get the usage percentage based on the number of hits and total calls
 *
 * @param calls the number of calls made
 * @param hits the number of cache hits when called
 * @returns the usage as a percentage string
 */
function getUsagePercentage(calls, hits) {
  return calls ? (hits / calls * 100).toFixed(4) + "%" : '0.0000%';
}

/**
 * @private
 *
 * @description
 * get the statistics for a given method or all methods
 *
 * @param [profileName] the profileName to get the statistics for (get all when not provided)
 * @returns the object with stats information
 */
function getStats(profileName) {
  if (!statsCache.isCollectingStats && !hasWarningDisplayed) {
    console.warn('Stats are not currently being collected, please run "collectStats" to enable them.'); // eslint-disable-line no-console

    hasWarningDisplayed = true;
  }
  var profiles = statsCache.profiles;
  if (profileName) {
    if (!profiles[profileName]) {
      return {
        calls: 0,
        hits: 0,
        usage: '0.0000%'
      };
    }
    var profile = profiles[profileName];
    return _extends({}, profile, {
      usage: getUsagePercentage(profile.calls, profile.hits)
    });
  }
  var completeStats = Object.keys(statsCache.profiles).reduce(function (completeProfiles, profileName) {
    completeProfiles.calls += profiles[profileName].calls;
    completeProfiles.hits += profiles[profileName].hits;
    return completeProfiles;
  }, {
    calls: 0,
    hits: 0
  });
  return _extends({}, completeStats, {
    profiles: Object.keys(profiles).reduce(function (computedProfiles, profileName) {
      computedProfiles[profileName] = getStats(profileName);
      return computedProfiles;
    }, {}),
    usage: getUsagePercentage(completeStats.calls, completeStats.hits)
  });
}

/**
 * @private
 *
 * @function getStatsOptions
 *
 * @description
 * get the options specific to storing statistics
 *
 * @param {Options} options the options passed to the moizer
 * @returns {Object} the options specific to keeping stats
 */
function getStatsOptions(options) {
  return statsCache.isCollectingStats ? {
    onCacheAdd: createOnCacheAddIncrementCalls(options),
    onCacheHit: createOnCacheHitIncrementCallsAndHits(options)
  } : {};
}

var ALWAYS_SKIPPED_PROPERTIES = {
  arguments: true,
  callee: true,
  caller: true,
  constructor: true,
  length: true,
  name: true,
  prototype: true
};

/**
 * @private
 *
 * @description
 * copy the static properties from the original function to the moized
 * function
 *
 * @param originalFn the function copying from
 * @param newFn the function copying to
 * @param skippedProperties the list of skipped properties, if any
 */
function copyStaticProperties(originalFn, newFn, skippedProperties) {
  if (skippedProperties === void 0) {
    skippedProperties = [];
  }
  Object.getOwnPropertyNames(originalFn).forEach(function (property) {
    if (!ALWAYS_SKIPPED_PROPERTIES[property] && skippedProperties.indexOf(property) === -1) {
      var descriptor = Object.getOwnPropertyDescriptor(originalFn, property);
      if (descriptor.get || descriptor.set) {
        Object.defineProperty(newFn, property, descriptor);
      } else {
        // @ts-expect-error - properites may not align
        newFn[property] = originalFn[property];
      }
    }
  });
}

/**
 * @private
 *
 * @description
 * add methods to the moized fuction object that allow extra features
 *
 * @param memoized the memoized function from micro-memoize
 */
function addInstanceMethods(memoized, _ref) {
  var expirations = _ref.expirations;
  var options = memoized.options;
  var findKeyIndex = createFindKeyIndex(options.isEqual, options.isMatchingKey);
  var moized = memoized;
  moized.clear = function () {
    var onCacheChange = moized._microMemoizeOptions.onCacheChange,
      cache = moized.cache;
    cache.keys.length = 0;
    cache.values.length = 0;
    if (onCacheChange) {
      onCacheChange(cache, moized.options, moized);
    }
    return true;
  };
  moized.clearStats = function () {
    clearStats(moized.options.profileName);
  };
  moized.get = function (key) {
    var transformKey = moized._microMemoizeOptions.transformKey,
      cache = moized.cache;
    var cacheKey = transformKey ? transformKey(key) : key;
    var keyIndex = findKeyIndex(cache.keys, cacheKey);
    return keyIndex !== -1 ? moized.apply(this, key) : undefined;
  };
  moized.getStats = function () {
    return getStats(moized.options.profileName);
  };
  moized.has = function (key) {
    var transformKey = moized._microMemoizeOptions.transformKey;
    var cacheKey = transformKey ? transformKey(key) : key;
    return findKeyIndex(moized.cache.keys, cacheKey) !== -1;
  };
  moized.keys = function () {
    return moized.cacheSnapshot.keys;
  };
  moized.remove = function (key) {
    var _moized$_microMemoize = moized._microMemoizeOptions,
      onCacheChange = _moized$_microMemoize.onCacheChange,
      transformKey = _moized$_microMemoize.transformKey,
      cache = moized.cache;
    var keyIndex = findKeyIndex(cache.keys, transformKey ? transformKey(key) : key);
    if (keyIndex === -1) {
      return false;
    }
    var existingKey = cache.keys[keyIndex];
    cache.keys.splice(keyIndex, 1);
    cache.values.splice(keyIndex, 1);
    if (onCacheChange) {
      onCacheChange(cache, moized.options, moized);
    }
    clearExpiration(expirations, existingKey, true);
    return true;
  };
  moized.set = function (key, value) {
    var _microMemoizeOptions = moized._microMemoizeOptions,
      cache = moized.cache,
      options = moized.options;
    var onCacheAdd = _microMemoizeOptions.onCacheAdd,
      onCacheChange = _microMemoizeOptions.onCacheChange,
      transformKey = _microMemoizeOptions.transformKey;
    var cacheKey = transformKey ? transformKey(key) : key;
    var keyIndex = findKeyIndex(cache.keys, cacheKey);
    if (keyIndex === -1) {
      var cutoff = options.maxSize - 1;
      if (cache.size > cutoff) {
        cache.keys.length = cutoff;
        cache.values.length = cutoff;
      }
      cache.keys.unshift(cacheKey);
      cache.values.unshift(value);
      if (options.isPromise) {
        cache.updateAsyncCache(moized);
      }
      if (onCacheAdd) {
        onCacheAdd(cache, options, moized);
      }
      if (onCacheChange) {
        onCacheChange(cache, options, moized);
      }
    } else {
      var existingKey = cache.keys[keyIndex];
      cache.values[keyIndex] = value;
      if (keyIndex > 0) {
        cache.orderByLru(existingKey, value, keyIndex);
      }
      if (options.isPromise) {
        cache.updateAsyncCache(moized);
      }
      if (typeof onCacheChange === 'function') {
        onCacheChange(cache, options, moized);
      }
    }
  };
  moized.values = function () {
    return moized.cacheSnapshot.values;
  };
}

/**
 * @private
 *
 * @description
 * add propeties to the moized fuction object that surfaces extra information
 *
 * @param memoized the memoized function
 * @param expirations the list of expirations for cache items
 * @param options the options passed to the moizer
 * @param originalFunction the function that is being memoized
 */
function addInstanceProperties(memoized, _ref2) {
  var expirations = _ref2.expirations,
    moizeOptions = _ref2.options,
    originalFunction = _ref2.originalFunction;
  var microMemoizeOptions = memoized.options;
  Object.defineProperties(memoized, {
    _microMemoizeOptions: {
      configurable: true,
      get: function get() {
        return microMemoizeOptions;
      }
    },
    cacheSnapshot: {
      configurable: true,
      get: function get() {
        var currentCache = memoized.cache;
        return {
          keys: currentCache.keys.slice(0),
          size: currentCache.size,
          values: currentCache.values.slice(0)
        };
      }
    },
    expirations: {
      configurable: true,
      get: function get() {
        return expirations;
      }
    },
    expirationsSnapshot: {
      configurable: true,
      get: function get() {
        return expirations.slice(0);
      }
    },
    isMoized: {
      configurable: true,
      get: function get() {
        return true;
      }
    },
    options: {
      configurable: true,
      get: function get() {
        return moizeOptions;
      }
    },
    originalFunction: {
      configurable: true,
      get: function get() {
        return originalFunction;
      }
    }
  });
  var moized = memoized;
  copyStaticProperties(originalFunction, moized);
}

/**
 * @private
 *
 * @description
 * add methods and properties to the memoized function for more features
 *
 * @param memoized the memoized function
 * @param configuration the configuration object for the instance
 * @returns the memoized function passed
 */
function createMoizeInstance(memoized, configuration) {
  addInstanceMethods(memoized, configuration);
  addInstanceProperties(memoized, configuration);
  return memoized;
}

// This was stolen from React internals, which allows us to create React elements without needing
// a dependency on the React library itself.
var REACT_ELEMENT_TYPE = typeof Symbol === 'function' && Symbol.for ? Symbol.for('react.element') : 0xeac7;

/**
 * @private
 *
 * @description
 * Create a component that memoizes based on `props` and legacy `context`
 * on a per-instance basis. This requires creating a component class to
 * store the memoized function. The cost is quite low, and avoids the
 * need to have access to the React dependency by basically re-creating
 * the basic essentials for a component class and the results of the
 * `createElement` function.
 *
 * @param moizer the top-level moize method
 * @param fn the component to memoize
 * @param options the memoization options
 * @returns the memoized component
 */
function createMoizedComponent(moizer, fn, options) {
  /**
   * This is a hack override setting the necessary options
   * for a React component to be memoized. In the main `moize`
   * method, if the `isReact` option is set it is short-circuited
   * to call this function, and these overrides allow the
   * necessary transformKey method to be derived.
   *
   * The order is based on:
   * 1) Set the necessary aspects of transformKey for React components.
   * 2) Allow setting of other options and overrides of those aspects
   *    if desired (for example, `isDeepEqual` will use deep equality).
   * 3) Always set `isReact` to false to prevent infinite loop.
   */
  var reactMoizer = moizer(_extends({
    maxArgs: 2,
    isShallowEqual: true
  }, options, {
    isReact: false
  }));
  if (!fn.displayName) {
    // @ts-ignore - allow setting of displayName
    fn.displayName = fn.name || 'Component';
  }
  function Moized(props, context, updater) {
    this.props = props;
    this.context = context;
    this.updater = updater;
    this.MoizedComponent = reactMoizer(fn);
  }
  Moized.prototype.isReactComponent = {};
  Moized.prototype.render = function () {
    return {
      $$typeof: REACT_ELEMENT_TYPE,
      type: this.MoizedComponent,
      props: this.props,
      ref: null,
      key: null,
      _owner: null
    };
  };
  copyStaticProperties(fn, Moized, ['contextType', 'contextTypes']);
  Moized.displayName = "Moized(" + (fn.displayName || fn.name || 'Component') + ")";
  setName(Moized, fn.name, options.profileName);
  return Moized;
}

function createGetInitialArgs(size) {
  /**
   * @private
   *
   * @description
   * take the first N number of items from the array (faster than slice)
   *
   * @param args the args to take from
   * @returns the shortened list of args as an array
   */
  return function (args) {
    if (size >= args.length) {
      return args;
    }
    if (size === 0) {
      return [];
    }
    if (size === 1) {
      return [args[0]];
    }
    if (size === 2) {
      return [args[0], args[1]];
    }
    if (size === 3) {
      return [args[0], args[1], args[2]];
    }
    var clone = [];
    for (var index = 0; index < size; index++) {
      clone[index] = args[index];
    }
    return clone;
  };
}

/**
 * @function getCutoff
 *
 * @description
 * faster `Array.prototype.indexOf` implementation build for slicing / splicing
 *
 * @param array the array to match the value in
 * @param value the value to match
 * @returns the matching index, or -1
 */
function getCutoff(array, value) {
  var length = array.length;
  for (var index = 0; index < length; ++index) {
    if (array[index] === value) {
      return index + 1;
    }
  }
  return 0;
}

/**
 * @private
 *
 * @description
 * custom replacer for the stringify function
 *
 * @returns if function then toString of it, else the value itself
 */
function createDefaultReplacer() {
  var cache = [];
  var keys = [];
  return function defaultReplacer(key, value) {
    var type = typeof value;
    if (type === 'function' || type === 'symbol') {
      return value.toString();
    }
    if (typeof value === 'object') {
      if (cache.length) {
        var thisCutoff = getCutoff(cache, this);
        if (thisCutoff === 0) {
          cache[cache.length] = this;
        } else {
          cache.splice(thisCutoff);
          keys.splice(thisCutoff);
        }
        keys[keys.length] = key;
        var valueCutoff = getCutoff(cache, value);
        if (valueCutoff !== 0) {
          return "[ref=" + (keys.slice(0, valueCutoff).join('.') || '.') + "]";
        }
      } else {
        cache[0] = value;
        keys[0] = key;
      }
      return value;
    }
    return '' + value;
  };
}

/**
 * @private
 *
 * @description
 * get the stringified version of the argument passed
 *
 * @param arg argument to stringify
 * @returns the stringified argument
 */
function getStringifiedArgument(arg) {
  var typeOfArg = typeof arg;
  return arg && (typeOfArg === 'object' || typeOfArg === 'function') ? JSON.stringify(arg, createDefaultReplacer()) : arg;
}

/**
 * @private
 *
 * @description
 * serialize the arguments passed
 *
 * @param options the options passed to the moizer
 * @param options.maxArgs the cap on the number of arguments used in serialization
 * @returns argument serialization method
 */
function defaultArgumentSerializer(args) {
  var key = '|';
  for (var index = 0; index < args.length; index++) {
    key += getStringifiedArgument(args[index]) + '|';
  }
  return [key];
}

/**
 * @private
 *
 * @description
 * based on the options passed, either use the serializer passed or generate the internal one
 *
 * @param options the options passed to the moized function
 * @returns the function to use in serializing the arguments
 */
function getSerializerFunction(options) {
  return typeof options.serializer === 'function' ? options.serializer : defaultArgumentSerializer;
}

/**
 * @private
 *
 * @description
 * are the serialized keys equal to one another
 *
 * @param cacheKey the cache key to compare
 * @param key the key to test
 * @returns are the keys equal
 */
function getIsSerializedKeyEqual(cacheKey, key) {
  return cacheKey[0] === key[0];
}

function createOnCacheOperation(fn) {
  if (typeof fn === 'function') {
    return function (_cacheIgnored, _microMemoizeOptionsIgnored, memoized) {
      return fn(memoized.cache, memoized.options, memoized);
    };
  }
}

/**
 * @private
 *
 * @description
 * get the isEqual method passed to micro-memoize
 *
 * @param options the options passed to the moizer
 * @returns the isEqual method to apply
 */
function getIsEqual(options) {
  return options.matchesArg || options.isDeepEqual && deepEqual || options.isShallowEqual && shallowEqual || sameValueZeroEqual;
}

/**
 * @private
 *
 * @description
 * get the isEqual method passed to micro-memoize
 *
 * @param options the options passed to the moizer
 * @returns the isEqual method to apply
 */
function getIsMatchingKey(options) {
  return options.matchesKey || options.isSerialized && getIsSerializedKeyEqual || undefined;
}

/**
 * @private
 *
 * @description
 * get the function that will transform the key based on the arguments passed
 *
 * @param options the options passed to the moizer
 * @returns the function to transform the key with
 */
function getTransformKey(options) {
  return compose(options.isSerialized && getSerializerFunction(options), typeof options.transformArgs === 'function' && options.transformArgs, typeof options.maxArgs === 'number' && createGetInitialArgs(options.maxArgs));
}

function createRefreshableMoized(moized) {
  var updateCacheForKey = moized.options.updateCacheForKey;

  /**
   * @private
   *
   * @description
   * Wrapper around already-`moize`d function which will intercept the memoization
   * and call the underlying function directly with the purpose of updating the cache
   * for the given key.
   *
   * Promise values use a tweak of the logic that exists at cache.updateAsyncCache, which
   * reverts to the original value if the promise is rejected and there was already a cached
   * value.
   */
  var refreshableMoized = function refreshableMoized() {
    for (var _len = arguments.length, args = new Array(_len), _key = 0; _key < _len; _key++) {
      args[_key] = arguments[_key];
    }
    if (!updateCacheForKey(args)) {
      return moized.apply(this, args);
    }
    var result = moized.fn.apply(this, args);
    moized.set(args, result);
    return result;
  };
  copyStaticProperties(moized, refreshableMoized);
  return refreshableMoized;
}

var _excluded = ["matchesArg", "isDeepEqual", "isPromise", "isReact", "isSerialized", "isShallowEqual", "matchesKey", "maxAge", "maxArgs", "maxSize", "onCacheAdd", "onCacheChange", "onCacheHit", "onExpire", "profileName", "serializer", "updateCacheForKey", "transformArgs", "updateExpire"];
/**
 * @module moize
 */
/**
 * @description
 * memoize a function based its arguments passed, potentially improving runtime performance
 *
 * @example
 * import moize from 'moize';
 *
 * // standard implementation
 * const fn = (foo, bar) => `${foo} ${bar}`;
 * const memoizedFn = moize(fn);
 *
 * // implementation with options
 * const fn = async (id) => get(`http://foo.com/${id}`);
 * const memoizedFn = moize(fn, {isPromise: true, maxSize: 5});
 *
 * // implementation with convenience methods
 * const Foo = ({foo}) => <div>{foo}</div>;
 * const MemoizedFoo = moize.react(Foo);
 *
 * @param fn the function to memoized, or a list of options when currying
 * @param [options=DEFAULT_OPTIONS] the options to apply
 * @returns the memoized function
 */
var moize = function moize(fn, passedOptions) {
  var options = passedOptions || DEFAULT_OPTIONS;
  if (isMoized(fn)) {
    var moizeable = fn.originalFunction;
    var mergedOptions = mergeOptions(fn.options, options);
    return moize(moizeable, mergedOptions);
  }
  if (typeof fn === 'object') {
    return function (curriedFn, curriedOptions) {
      if (typeof curriedFn === 'function') {
        var _mergedOptions = mergeOptions(fn, curriedOptions);
        return moize(curriedFn, _mergedOptions);
      }
      var mergedOptions = mergeOptions(fn, curriedFn);
      return moize(mergedOptions);
    };
  }
  if (options.isReact) {
    return createMoizedComponent(moize, fn, options);
  }
  var coalescedOptions = _extends({}, DEFAULT_OPTIONS, options, {
    maxAge: typeof options.maxAge === 'number' && options.maxAge >= 0 ? options.maxAge : DEFAULT_OPTIONS.maxAge,
    maxArgs: typeof options.maxArgs === 'number' && options.maxArgs >= 0 ? options.maxArgs : DEFAULT_OPTIONS.maxArgs,
    maxSize: typeof options.maxSize === 'number' && options.maxSize >= 0 ? options.maxSize : DEFAULT_OPTIONS.maxSize,
    profileName: options.profileName || getDefaultProfileName(fn)
  });
  var expirations = [];
  coalescedOptions.matchesArg;
    coalescedOptions.isDeepEqual;
    var isPromise = coalescedOptions.isPromise;
    coalescedOptions.isReact;
    coalescedOptions.isSerialized;
    coalescedOptions.isShallowEqual;
    coalescedOptions.matchesKey;
    coalescedOptions.maxAge;
    coalescedOptions.maxArgs;
    var maxSize = coalescedOptions.maxSize,
    onCacheAdd = coalescedOptions.onCacheAdd,
    onCacheChange = coalescedOptions.onCacheChange,
    onCacheHit = coalescedOptions.onCacheHit;
    coalescedOptions.onExpire;
    coalescedOptions.profileName;
    coalescedOptions.serializer;
    var updateCacheForKey = coalescedOptions.updateCacheForKey;
    coalescedOptions.transformArgs;
    coalescedOptions.updateExpire;
    var customOptions = _objectWithoutPropertiesLoose(coalescedOptions, _excluded);
  var isEqual = getIsEqual(coalescedOptions);
  var isMatchingKey = getIsMatchingKey(coalescedOptions);
  var maxAgeOptions = getMaxAgeOptions(expirations, coalescedOptions, isEqual, isMatchingKey);
  var statsOptions = getStatsOptions(coalescedOptions);
  var transformKey = getTransformKey(coalescedOptions);
  var microMemoizeOptions = _extends({}, customOptions, {
    isEqual: isEqual,
    isMatchingKey: isMatchingKey,
    isPromise: isPromise,
    maxSize: maxSize,
    onCacheAdd: createOnCacheOperation(combine(onCacheAdd, maxAgeOptions.onCacheAdd, statsOptions.onCacheAdd)),
    onCacheChange: createOnCacheOperation(onCacheChange),
    onCacheHit: createOnCacheOperation(combine(onCacheHit, maxAgeOptions.onCacheHit, statsOptions.onCacheHit)),
    transformKey: transformKey
  });
  var memoized = memoize(fn, microMemoizeOptions);
  var moized = createMoizeInstance(memoized, {
    expirations: expirations,
    options: coalescedOptions,
    originalFunction: fn
  });
  if (updateCacheForKey) {
    moized = createRefreshableMoized(moized);
  }
  setName(moized, fn.name, options.profileName);
  return moized;
};

/**
 * @function
 * @name clearStats
 * @memberof module:moize
 * @alias moize.clearStats
 *
 * @description
 * clear all existing stats stored
 */
moize.clearStats = clearStats;

/**
 * @function
 * @name collectStats
 * @memberof module:moize
 * @alias moize.collectStats
 *
 * @description
 * start collecting statistics
 */
moize.collectStats = collectStats;

/**
 * @function
 * @name compose
 * @memberof module:moize
 * @alias moize.compose
 *
 * @description
 * method to compose moized methods and return a single moized function
 *
 * @param moized the functions to compose
 * @returns the composed function
 */
moize.compose = function () {
  return compose.apply(void 0, arguments) || moize;
};

/**
 * @function
 * @name deep
 * @memberof module:moize
 * @alias moize.deep
 *
 * @description
 * should deep equality check be used
 *
 * @returns the moizer function
 */
moize.deep = moize({
  isDeepEqual: true
});

/**
 * @function
 * @name getStats
 * @memberof module:moize
 * @alias moize.getStats
 *
 * @description
 * get the statistics of a given profile, or overall usage
 *
 * @returns statistics for a given profile or overall usage
 */
moize.getStats = getStats;

/**
 * @function
 * @name infinite
 * @memberof module:moize
 * @alias moize.infinite
 *
 * @description
 * a moized method that will remove all limits from the cache size
 *
 * @returns the moizer function
 */
moize.infinite = moize({
  maxSize: Infinity
});

/**
 * @function
 * @name isCollectingStats
 * @memberof module:moize
 * @alias moize.isCollectingStats
 *
 * @description
 * are stats being collected
 *
 * @returns are stats being collected
 */
moize.isCollectingStats = function isCollectingStats() {
  return statsCache.isCollectingStats;
};

/**
 * @function
 * @name isMoized
 * @memberof module:moize
 * @alias moize.isMoized
 *
 * @description
 * is the fn passed a moized function
 *
 * @param fn the object to test
 * @returns is fn a moized function
 */
moize.isMoized = function isMoized(fn) {
  return typeof fn === 'function' && !!fn.isMoized;
};

/**
 * @function
 * @name matchesArg
 * @memberof module:moize
 * @alias moize.matchesArg
 *
 * @description
 * a moized method where the arg matching method is the custom one passed
 *
 * @param keyMatcher the method to compare against those in cache
 * @returns the moizer function
 */
moize.matchesArg = function (argMatcher) {
  return moize({
    matchesArg: argMatcher
  });
};

/**
 * @function
 * @name matchesKey
 * @memberof module:moize
 * @alias moize.matchesKey
 *
 * @description
 * a moized method where the key matching method is the custom one passed
 *
 * @param keyMatcher the method to compare against those in cache
 * @returns the moizer function
 */
moize.matchesKey = function (keyMatcher) {
  return moize({
    matchesKey: keyMatcher
  });
};
function maxAge(maxAge, expireOptions) {
  if (expireOptions === true) {
    return moize({
      maxAge: maxAge,
      updateExpire: expireOptions
    });
  }
  if (typeof expireOptions === 'object') {
    var onExpire = expireOptions.onExpire,
      updateExpire = expireOptions.updateExpire;
    return moize({
      maxAge: maxAge,
      onExpire: onExpire,
      updateExpire: updateExpire
    });
  }
  if (typeof expireOptions === 'function') {
    return moize({
      maxAge: maxAge,
      onExpire: expireOptions,
      updateExpire: true
    });
  }
  return moize({
    maxAge: maxAge
  });
}

/**
 * @function
 * @name maxAge
 * @memberof module:moize
 * @alias moize.maxAge
 *
 * @description
 * a moized method where the age of the cache is limited to the number of milliseconds passed
 *
 * @param maxAge the TTL of the value in cache
 * @returns the moizer function
 */
moize.maxAge = maxAge;

/**
 * @function
 * @name maxArgs
 * @memberof module:moize
 * @alias moize.maxArgs
 *
 * @description
 * a moized method where the number of arguments used for determining cache is limited to the value passed
 *
 * @param maxArgs the number of args to base the key on
 * @returns the moizer function
 */
moize.maxArgs = function maxArgs(maxArgs) {
  return moize({
    maxArgs: maxArgs
  });
};

/**
 * @function
 * @name maxSize
 * @memberof module:moize
 * @alias moize.maxSize
 *
 * @description
 * a moized method where the total size of the cache is limited to the value passed
 *
 * @param maxSize the maximum size of the cache
 * @returns the moizer function
 */
moize.maxSize = function maxSize(maxSize) {
  return moize({
    maxSize: maxSize
  });
};

/**
 * @function
 * @name profile
 * @memberof module:moize
 * @alias moize.profile
 *
 * @description
 * a moized method with a profile name
 *
 * @returns the moizer function
 */
moize.profile = function (profileName) {
  return moize({
    profileName: profileName
  });
};

/**
 * @function
 * @name promise
 * @memberof module:moize
 * @alias moize.promise
 *
 * @description
 * a moized method specific to caching resolved promise / async values
 *
 * @returns the moizer function
 */
moize.promise = moize({
  isPromise: true,
  updateExpire: true
});

/**
 * @function
 * @name react
 * @memberof module:moize
 * @alias moize.react
 *
 * @description
 * a moized method specific to caching React element values
 *
 * @returns the moizer function
 */
moize.react = moize({
  isReact: true
});

/**
 * @function
 * @name serialize
 * @memberof module:moize
 * @alias moize.serialize
 *
 * @description
 * a moized method that will serialize the arguments passed to use as the cache key
 *
 * @returns the moizer function
 */
moize.serialize = moize({
  isSerialized: true
});

/**
 * @function
 * @name serializeWith
 * @memberof module:moize
 * @alias moize.serializeWith
 *
 * @description
 * a moized method that will serialize the arguments passed to use as the cache key
 * based on the serializer passed
 *
 * @returns the moizer function
 */
moize.serializeWith = function (serializer) {
  return moize({
    isSerialized: true,
    serializer: serializer
  });
};

/**
 * @function
 * @name shallow
 * @memberof module:moize
 * @alias moize.shallow
 *
 * @description
 * should shallow equality check be used
 *
 * @returns the moizer function
 */
moize.shallow = moize({
  isShallowEqual: true
});

/**
 * @function
 * @name transformArgs
 * @memberof module:moize
 * @alias moize.transformArgs
 *
 * @description
 * transform the args to allow for specific cache key comparison
 *
 * @param transformArgs the args transformer
 * @returns the moizer function
 */
moize.transformArgs = function (transformArgs) {
  return moize({
    transformArgs: transformArgs
  });
};

/**
 * @function
 * @name updateCacheForKey
 * @memberof module:moize
 * @alias moize.updateCacheForKey
 *
 * @description
 * update the cache for a given key when the method passed returns truthy
 *
 * @param updateCacheForKey the method to determine when to update cache
 * @returns the moizer function
 */
moize.updateCacheForKey = function (updateCacheForKey) {
  return moize({
    updateCacheForKey: updateCacheForKey
  });
};

// Add self-referring `default` property for edge-case cross-compatibility of mixed ESM/CommonJS usage.
// This property is frozen and non-enumerable to avoid visibility on iteration or accidental overrides.
Object.defineProperty(moize, 'default', {
  configurable: false,
  enumerable: false,
  value: moize,
  writable: false
});

export { moize as default };
//# sourceMappingURL=index.mjs.map
