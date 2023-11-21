'use strict';


var fn     = require('./function');
var undef  = require('./undefined');
var regexp = require('./regexp');

module.exports.function  = fn;
module.exports.undefined = undef;
module.exports.regexp    = regexp;

module.exports.all = [ fn, undef, regexp ];
