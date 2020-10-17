package fr.pedrokarim.ascencia.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AnnoCommand {

    public String name();
    public String description() default "None.";
    public AnnoCommand.ExecutorType type() default AnnoCommand.ExecutorType.ALL;
    public String[] aliases();
    public String category();
    public String[] permissions();
    public boolean test() default false;
    public boolean disabled() default false;
    public boolean nfsw() default false;

    public enum ExecutorType{
        ALL, USER, OWNER, CONSOLE;
    }
}