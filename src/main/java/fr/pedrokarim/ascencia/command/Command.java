package fr.pedrokarim.ascencia.command;

import java.lang.reflect.Method;
import java.util.Map;

public final class Command {

    private final String name, description, category;
    private final AnnoCommand.ExecutorType executorType;
    private final String[] aliases;
    private final String[] permissions;
    private final boolean test;
    private final boolean disabled;
    private final boolean nfsw;

    private final Object object;
    private final Method method;

    public Command(Map<String, Object> config, Object object, Method method) {
        this.name = (String) config.get("name");
        this.description = (String) config.get("description");
        this.executorType = (AnnoCommand.ExecutorType) config.get("executorType");
        this.aliases = (String[]) config.get("aliases");
        this.category = (String) config.get("category");
        this.permissions = (String[]) config.get("permissions");
        this.test = (boolean) config.get("test");
        this.disabled = (boolean) config.get("disabled");
        this.nfsw = (boolean) config.get("nfsw");

        this.object = object;
        this.method = method;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public AnnoCommand.ExecutorType getExecutorType() {
        return executorType;
    }

    public String[] getAliases() {
        return aliases;
    }

    public String getCategory() {
        return category;
    }

    public String[] getPermissions() {
        return permissions;
    }

    public boolean isTest() {
        return test;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public boolean isNfsw() {
        return nfsw;
    }

    public Object getObject() {
        return object;
    }

    public Method getMethod() {
        return method;
    }
}
