package test.java.club.mcams.carpet;

import club.mcams.carpet.translations.AMSTranslations;

public class TranslationTest {
    public static void main(String[] args) {
        AMSTranslations.loadTranslations();
        System.out.println(AMSTranslations.getTranslation("zh_cn"));
    }
}
