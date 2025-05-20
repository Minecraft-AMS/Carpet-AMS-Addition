/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025 A Minecraft Server and contributors
 *
 * Carpet AMS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet AMS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet AMS Addition. If not, see <https://www.gnu.org/licenses/>.
 */

package club.mcams.carpet.yaml;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YamlParser {
    // 用于YAML解析的正则表达式
    private static final Pattern KEY_VALUE_PATTERN = Pattern.compile("^(\\s*)(['\"]?\\S[^:]*?['\"]?)\\s*:\\s*(.*)$");
    private static final Pattern LIST_ITEM_PATTERN = Pattern.compile("^(\\s*)-\\s+(.*)$");
    private static final Pattern QUOTED_STRING_PATTERN = Pattern.compile("^[\"'](.*)[\"']$");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("^-?\\d+(\\.\\d+)?$");
    private static final Pattern BOOLEAN_PATTERN = Pattern.compile("^(true|false)$", Pattern.CASE_INSENSITIVE);

    /**
     * 去除字符串的引号（如果存在）
     *
     * @param str 可能包含引号的字符串
     * @return 去除引号后的字符串
     */
    private static String removeQuotes(String str) {
        if (str == null || str.length() < 2) {
            return str;
        }

        char first = str.charAt(0);
        char last = str.charAt(str.length() - 1);

        if ((first == '"' && last == '"') || (first == '\'' && last == '\'')) {
            return str.substring(1, str.length() - 1);
        }

        return str;
    }

    /**
     * 从字符串解析YAML内容
     *
     * @param content YAML格式的字符串内容
     * @return 解析后的Map结构
     * @throws IOException 读取内容时发生错误
     * @throws YamlParseException 解析YAML时发生错误
     */
    public static Map<String, Object> parse(String content) throws IOException, YamlParseException {
        try (Reader reader = new StringReader(content)) {
            return parse(reader);
        }
    }

    /**
     * 从Reader解析YAML内容
     *
     * @param reader 包含YAML内容的Reader对象
     * @return 解析后的Map结构
     * @throws IOException 读取时发生错误
     * @throws YamlParseException 解析YAML时发生错误
     */
    public static Map<String, Object> parse(Reader reader) throws IOException, YamlParseException {
        Map<String, Object> result = new LinkedHashMap<>();
        BufferedReader bufferedReader = new BufferedReader(reader);

        List<String> lines = new ArrayList<>();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            // 跳过空行和注释
            if (line.trim().isEmpty() || line.trim().startsWith("#")) {
                continue;
            }
            lines.add(line);
        }

        parseNode(lines, 0, 0, result);
        return result;
    }

    /**
     * 递归解析YAML节点
     *
     * @param lines 所有YAML内容行
     * @param startIndex 开始解析的索引
     * @param baseIndentation 当前节点的缩进级别
     * @param container 存放解析结果的容器
     * @return 下一个要解析的行索引
     * @throws YamlParseException 解析YAML时发生错误
     */
    private static int parseNode(List<String> lines, int startIndex, int baseIndentation, Map<String, Object> container) throws YamlParseException {
        int i = startIndex;

        while (i < lines.size()) {
            String line = lines.get(i);
            int indentation = countIndentation(line);

            // 如果缩进级别小于当前基准缩进，则返回上级
            if (indentation < baseIndentation && i > startIndex) {
                return i;
            }

            // 检查是否为键值对
            Matcher keyValueMatcher = KEY_VALUE_PATTERN.matcher(line);
            if (keyValueMatcher.matches()) {
                String keyWithQuotes = keyValueMatcher.group(2);
                String key = removeQuotes(keyWithQuotes); // 去除可能存在的引号
                String value = keyValueMatcher.group(3);

                // 处理空值或可能包含嵌套结构的情况
                if (value.isEmpty()) {
                    i++;
                    // 检查下一行是否有更大的缩进（表示嵌套结构）
                    if (i < lines.size() && countIndentation(lines.get(i)) > indentation) {
                        // 解析嵌套结构
                        if (lines.get(i).trim().startsWith("-")) {
                            // 列表结构
                            List<Object> list = new ArrayList<>();
                            i = parseList(lines, i, indentation + 2, list);
                            container.put(key, list);
                        } else {
                            // Map结构
                            Map<String, Object> nestedMap = new LinkedHashMap<>();
                            i = parseNode(lines, i, indentation + 2, nestedMap);
                            container.put(key, nestedMap);
                        }
                    } else {
                        // 空值
                        container.put(key, "");
                    }
                } else {
                    // 处理直接值
                    container.put(key, parseValue(value));
                    i++;
                }
            } else {
                // 检查是否为列表项
                Matcher listItemMatcher = LIST_ITEM_PATTERN.matcher(line);
                if (listItemMatcher.matches() && indentation == baseIndentation) {
                    throw new YamlParseException("Unexpected list item, line number:" + (i + 1));
                } else {
                    i++;
                }
            }
        }

        return i;
    }

    /**
     * 解析YAML列表
     *
     * @param lines 所有YAML内容行
     * @param startIndex 开始解析的索引
     * @param baseIndentation 列表的基准缩进级别
     * @param list 存放解析结果的列表
     * @return 下一个要解析的行索引
     * @throws YamlParseException 解析YAML时发生错误
     */
    private static int parseList(List<String> lines, int startIndex, int baseIndentation, List<Object> list) throws YamlParseException {
        int i = startIndex;

        while (i < lines.size()) {
            String line = lines.get(i);
            int indentation = countIndentation(line);

            // 如果缩进级别小于基准缩进，返回上级
            if (indentation < baseIndentation) {
                return i;
            }

            Matcher listItemMatcher = LIST_ITEM_PATTERN.matcher(line);
            if (listItemMatcher.matches() && indentation == baseIndentation) {
                String value = listItemMatcher.group(2);

                if (value.isEmpty()) {
                    i++;
                    // 检查是否存在嵌套结构
                    if (i < lines.size() && countIndentation(lines.get(i)) > indentation) {
                        if (lines.get(i).trim().startsWith("-")) {
                            // 嵌套列表
                            List<Object> nestedList = new ArrayList<>();
                            i = parseList(lines, i, indentation + 2, nestedList);
                            list.add(nestedList);
                        } else {
                            // 嵌套Map
                            Map<String, Object> nestedMap = new LinkedHashMap<>();
                            i = parseNode(lines, i, indentation + 2, nestedMap);
                            list.add(nestedMap);
                        }
                    } else {
                        // 空列表项
                        list.add("");
                    }
                } else {
                    // 处理直接值
                    list.add(parseValue(value));
                    i++;
                }
            } else if (indentation > baseIndentation) {
                // 处理列表项下的缩进内容
                i++;
            } else {
                return i;
            }
        }

        return i;
    }

    /**
     * 解析YAML值
     *
     * @param valueStr 要解析的值字符串
     * @return 解析后的Java对象
     */
    private static Object parseValue(String valueStr) {
        if (valueStr == null || valueStr.isEmpty()) {
            return "";
        }

        String trimmedValue = valueStr.trim();

        // 处理带引号的字符串
        Matcher quotedMatcher = QUOTED_STRING_PATTERN.matcher(trimmedValue);
        if (quotedMatcher.matches()) {
            return quotedMatcher.group(1);
        }

        // 处理布尔值
        Matcher booleanMatcher = BOOLEAN_PATTERN.matcher(trimmedValue);
        if (booleanMatcher.matches()) {
            return Boolean.parseBoolean(trimmedValue.toLowerCase());
        }

        // 处理数字
        Matcher numberMatcher = NUMBER_PATTERN.matcher(trimmedValue);
        if (numberMatcher.matches()) {
            if (trimmedValue.contains(".")) {
                return Double.parseDouble(trimmedValue);
            } else {
                return Integer.parseInt(trimmedValue);
            }
        }

        // 处理null值
        if (trimmedValue.equalsIgnoreCase("null") || trimmedValue.equals("~")) {
            return null;
        }

        // 默认返回字符串
        return trimmedValue;
    }

    /**
     * 计算行的缩进级别
     *
     * @param line 要计算缩进的行
     * @return 行首的空格数量
     */
    private static int countIndentation(String line) {
        int count = 0;
        while (count < line.length() && line.charAt(count) == ' ') {
            count++;
        }
        return count;
    }

    /**
     * 将Map转换为YAML格式字符串
     *
     * @param data 要转换的数据
     * @return YAML格式的字符串
     */
    @SuppressWarnings("unused")
    public static String dump(Map<String, Object> data) {
        StringBuilder builder = new StringBuilder();
        writeMap(data, "", builder);
        return builder.toString();
    }

    /**
     * 将Map写入StringBuilder生成YAML
     *
     * @param map 要写入的Map
     * @param indent 当前缩进
     * @param builder 目标StringBuilder
     */
    private static void writeMap(Map<String, Object> map, String indent, StringBuilder builder) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            builder.append(indent).append(entry.getKey()).append(":");
            writeValue(entry.getValue(), indent, builder);
        }
    }

    /**
     * 将List写入StringBuilder生成YAML
     *
     * @param list 要写入的List
     * @param indent 当前缩进
     * @param builder 目标StringBuilder
     */
    @SuppressWarnings("unchecked")
    private static void writeList(List<?> list, String indent, StringBuilder builder) {
        for (Object item : list) {
            builder.append(indent).append("- ");
            if (item instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) item;
                if (map.isEmpty()) {
                    builder.append("{}\n");
                } else {
                    builder.append("\n");
                    writeMap(map, indent + "  ", builder);
                }
            } else if (item instanceof List) {
                List<Object> nestedList = (List<Object>) item;
                if (nestedList.isEmpty()) {
                    builder.append("[]\n");
                } else {
                    builder.append("\n");
                    writeList(nestedList, indent + "  ", builder);
                }
            } else {
                writeScalar(item, builder);
                builder.append("\n");
            }
        }
    }

    /**
     * 写入值到StringBuilder生成YAML
     *
     * @param value 要写入的值
     * @param indent 当前缩进
     * @param builder 目标StringBuilder
     */
    @SuppressWarnings("unchecked")
    private static void writeValue(Object value, String indent, StringBuilder builder) {
        if (value instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) value;
            if (map.isEmpty()) {
                builder.append(" {}\n");
            } else {
                builder.append("\n");
                writeMap(map, indent + "  ", builder);
            }
        } else if (value instanceof List) {
            List<Object> list = (List<Object>) value;
            if (list.isEmpty()) {
                builder.append(" []\n");
            } else {
                builder.append("\n");
                writeList(list, indent + "  ", builder);
            }
        } else {
            builder.append(" ");
            writeScalar(value, builder);
            builder.append("\n");
        }
    }

    /**
     * 写入标量值到StringBuilder，自动处理格式
     *
     * @param value 要写入的值
     * @param builder 目标StringBuilder
     */
    @SuppressWarnings("PatternVariableCanBeUsed")
    private static void writeScalar(Object value, StringBuilder builder) {
        if (value == null) {
            builder.append("null");
        } else if (value instanceof String) {
            String str = (String) value;
            if (needsQuoting(str)) {
                builder.append("\"").append(escapeString(str)).append("\"");
            } else {
                builder.append(str);
            }
        } else {
            builder.append(value);
        }
    }

    /**
     * 检查字符串是否需要加引号
     *
     * @param str 要检查的字符串
     * @return 如果需要加引号返回true，否则false
     */
    private static boolean needsQuoting(String str) {
        // 空字符串需要引号
        if (str.isEmpty()) {
            return true;
        }

        // 以特殊字符开头需要引号
        char firstChar = str.charAt(0);
        if ("&*-!|>%@`".indexOf(firstChar) >= 0 || Character.isDigit(firstChar) || firstChar == '?' || firstChar == ':') {
            return true;
        }

        // 包含特殊字符需要引号
        if (str.contains(":") || str.contains("#") || str.contains("\n") || str.trim().isEmpty() || str.contains("\"") || str.contains("'")) {
            return true;
        }

        // 可能被解释为其他类型的值需要引号
        return
            str.equalsIgnoreCase("true") ||
            str.equalsIgnoreCase("false") ||
            str.equalsIgnoreCase("null") ||
            str.equals("~") ||
            NUMBER_PATTERN.matcher(str).matches();
    }

    /**
     * 转义YAML字符串中的特殊字符
     *
     * @param str 要转义的字符串
     * @return 转义后的字符串
     */
    private static String escapeString(String str) {
        return
            str.replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t");
    }

    /**
     * 使用点符号路径从嵌套Map结构中获取值
     *
     * @param map 要查找的Map
     * @param path 点符号路径（例如："parent.child.key"）
     * @return 指定路径的值，如果不存在返回null
     */
    @Nullable
    public static Object getNestedValue(Map<String, Object> map, String path) {
        String[] parts = path.split("\\.");
        Object current = map;

        for (String part : parts) {
            if (current instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> currentMap = (Map<String, Object>) current;
                current = currentMap.get(part);
                if (current == null) {
                    return null;
                }
            } else {
                return null;
            }
        }

        return current;
    }

    /**
     * 从嵌套Map结构中获取字符串值
     *
     * @param map 要查找的Map
     * @param path 点符号路径
     * @return 字符串值，如果不存在或类型不符返回null
     */
    @SuppressWarnings("unused")
    @Nullable
    public static String getNestedString(Map<String, Object> map, String path) {
        Object value = getNestedValue(map, path);
        return value instanceof String ? (String) value : null;
    }

    /**
     * 从嵌套Map结构中获取字符串列表
     *
     * @param map 要查找的Map
     * @param path 点符号路径
     * @return 字符串列表，如果不存在返回空列表
     */
    @SuppressWarnings("PatternVariableCanBeUsed")
    @NotNull
    public static List<String> getNestedStringList(Map<String, Object> map, String path) {
        Object value = getNestedValue(map, path);

        if (value instanceof List) {
            List<?> listValue = (List<?>) value;
            List<String> result = new ArrayList<>();

            for (Object item : listValue) {
                if (item instanceof String) {
                    result.add((String) item);
                }
            }

            return result;
        }

        return new ArrayList<>();
    }
}
