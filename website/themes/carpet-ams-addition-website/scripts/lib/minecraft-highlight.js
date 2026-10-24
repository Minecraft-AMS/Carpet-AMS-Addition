'use strict';

const { unescapeHTML } = require('hexo-util');

const escape = text => text.replace(/[&<>"']/g, character => ({
  '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;',
})[character]);
const span = (type, text) => `<span class="mc-${type}">${escape(text)}</span>`;
const keywords = new Set((
  'add remove removeAll list help show info config reset create clear enabled time range '
  + 'group condition mode test direction offset is not all any up down north south east west '
  + 'northeast northwest southeast southwest set setDefault removeDefault true false ops '
  + 'as at run if unless positioned rotated facing anchored in align store result success '
  + 'entity block blocks score matches data get merge modify append prepend insert value '
  + 'spawn kill move stop use attack jump look turn mount dismount sneak sprint '
  + 'give take query start cancel marker sign rescan dimension'
).split(' '));

// 单次扫描并转义原文，不把指令参数或 NBT 当作 HTML。
function highlightCommands(source) {
  return source.split('\n').map(line => {
    if (/^\s*#/.test(line)) return span('comment', line);
    const command = line.match(/^(\s*)(\/?[a-zA-Z][\w:-]*)(?=\s|$)/);
    let result = command ? escape(command[1]) + span('command', command[2]) : '';
    const rest = command ? line.slice(command[0].length) : line;
    const tokens = /"(?:\\.|[^"\\])*"|'(?:\\.|[^'\\])*'|<[^<>\r\n]+>|@[paresn](?!\w)|#?[a-z0-9_.-]+:[a-z0-9_./-]+|[~^](?:-?\d+(?:\.\d+)?)?|-?\b\d+(?:\.\d+)?(?:[bBsSlLfFdD])?\b|\b[a-zA-Z_][\w.-]*\b/g;
    let index = 0;
    for (const match of rest.matchAll(tokens)) {
      result += escape(rest.slice(index, match.index));
      const token = match[0];
      let type;
      if (/^["']/.test(token)) type = 'string';
      else if (token.startsWith('<')) type = 'parameter';
      else if (token.startsWith('@')) type = 'selector';
      else if (token.includes(':')) type = 'resource';
      else if (/^[~^\d-]/.test(token)) type = 'number';
      else if (/^(true|false|null)$/.test(token)) type = 'literal';
      else if (/^\s*=/.test(rest.slice(match.index + token.length))) type = 'property';
      else if (keywords.has(token)) type = 'keyword';
      result += type ? span(type, token) : escape(token);
      index = match.index + token.length;
    }
    return result + escape(rest.slice(index));
  }).join('\n');
}

function highlightCommandBlocks(html) {
  return html.replace(/<code\b([^>]*)>([\s\S]*?)<\/code>/g, (original, attributes, body) => {
    const classes = attributes.match(/\bclass="([^"]*)"/)?.[1]?.split(/\s+/) || [];
    if (!classes.includes('hljs')) return original;
    const languages = classes.filter(name => name !== 'hljs').map(name => name.replace(/^language-/, ''));
    const explicit = languages.some(name => ['mcfunction', 'minecraft', 'mccommand'].includes(name));
    if (!explicit && languages.some(name => !['text', 'plaintext', 'plain', 'txt'].includes(name))) return original;
    const lineBreak = /<br\s*\/?>/i.test(body) ? '<br>' : '\n';
    const source = unescapeHTML(body.replace(/<br\s*\/?>/gi, '\n').replace(/<\/?span\b[^>]*>/g, ''));
    const lines = source.split('\n').filter(line => line.trim() && !/^\s*#/.test(line));
    if (!explicit && (!lines.length || !lines.every(line => /^\s*\/[a-zA-Z][\w:-]*(?=\s|$)/.test(line)))) return original;
    if (!classes.includes('mcfunction')) classes.push('mcfunction');
    return `<code${attributes.replace(/\bclass="[^"]*"/, `class="${classes.join(' ')}"`)}>${highlightCommands(source).replace(/\n/g, lineBreak)}</code>`;
  });
}

module.exports = { highlightCommands, highlightCommandBlocks };
