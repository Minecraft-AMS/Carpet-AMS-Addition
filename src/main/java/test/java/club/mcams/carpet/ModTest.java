/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  A Minecraft Server and contributors
 *
 * Carpet AMS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet AMS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet AMS Addition.  If not, see <https://www.gnu.org/licenses/>.
 */

package test.java.club.mcams.carpet;

import carpetamsaddition.translations.AMSTranslations;
import carpetamsaddition.utils.CountRulesUtil;

import java.io.File;

public class ModTest {
    public static void main(String[] args) {
        // 打印指定文件夹下的所有文件名
        String directoryPath = "path";
        printFilesName(directoryPath);

        // 测试翻译
        AMSTranslations.loadTranslations();
        System.out.println(AMSTranslations.getTranslation("zh_cn"));

        // 获取当前可用规则数量
        int ruleCount = CountRulesUtil.countRules();
        System.out.println("@Rule: " + ruleCount);
    }

    public static void printFilesName(String path) {
        File directory = new File(path);
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        printFilesName(file.getAbsolutePath());
                    } else {
                        System.out.println("\"" + file.getName() + "\",");
                    }
                }
            }
        }
    }
}
