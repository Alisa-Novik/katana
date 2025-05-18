package com.norwood.routing;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Route {
    public enum HttpMethod {
        GET,
        POST,
        PUT,
        DELETE,
        PATCH;

        public static HttpMethod fromString(String method) {
            if (method == null) {
                throw new IllegalArgumentException("HTTP method cannot be null");
            }
            return HttpMethod.valueOf(method.toUpperCase(Locale.ROOT));
        }

        @Override
        public String toString() {
            return this.name();
        }
    }

    private static final Pattern PLACEHOLDER = Pattern.compile("\\{([^/]+)\\}");

    private final HttpMethod method;
    private final String pattern;
    private final Pattern regex;
    private final List<String> parameterNames;
    private final Method handlerMethod;

    private Route(HttpMethod method, String pattern, Method handlerMethod) {
        this.method = method;
        this.pattern = pattern;
        this.handlerMethod = handlerMethod;

        Matcher matcher = PLACEHOLDER.matcher(pattern);
        StringBuffer sb = new StringBuffer();
        List<String> names = new ArrayList<>();
        while (matcher.find()) {
            names.add(matcher.group(1));
            matcher.appendReplacement(sb, "([^/]+)");
        }
        matcher.appendTail(sb);
        this.regex = Pattern.compile("^" + sb.toString() + "$");
        this.parameterNames = List.copyOf(names);
    }

    private static Route create(HttpMethod method, String pattern, Method handlerMethod) {
        return new Route(method, pattern, handlerMethod);
    }

    public boolean ofPath(String path) {
        return this.pattern.equals(path);
    }

    public boolean matches(String path) {
        return regex.matcher(path).matches();
    }

    public Map<String, String> extract(String path) {
        Matcher m = regex.matcher(path);
        if (!m.matches()) {
            return Map.of();
        }
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < parameterNames.size(); i++) {
            map.put(parameterNames.get(i), m.group(i + 1));
        }
        return map;
    }

    public static Route get(String pattern, Method method) {
        return Route.create(HttpMethod.GET, pattern, method);
    }

    public static Route post(String pattern, Method method) {
        return Route.create(HttpMethod.POST, pattern, method);
    }

    @Override
    public String toString() {
        return "Route '" + method.toString() + " " + pattern + "'";
    }

    public Object invoke(Object controller, HttpRequest request, Map<String, String> params) {
        try {
            Class<?>[] types = handlerMethod.getParameterTypes();
            Object[] args = new Object[types.length];
            int paramIdx = 0;
            for (int i = 0; i < types.length; i++) {
                if (HttpRequest.class.isAssignableFrom(types[i])) {
                    args[i] = request;
                } else {
                    String name = parameterNames.get(paramIdx++);
                    args[i] = params.get(name);
                }
            }
            return handlerMethod.invoke(controller, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public String path() {
        return pattern;
    }

}
