package club.mcams.carpet.util.recipes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CraftingRule {

    String name() default "";

    String[] recipes();

    String recipeNamespace() default "ams";
}
