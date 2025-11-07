package com.tmq.validator;

import com.tmq.exception.NotCorrectInputException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RequestValidator {
    public static Map<String, Integer> validateRequestNumberParams(Map<String,String[]> paramsMap, String... neededParams) {
        Map<String, Integer> map = new HashMap<>();

        try {
            Arrays.stream(neededParams).forEach(neededParam -> {
                if (!paramsMap.containsKey(neededParam)) throw new NotCorrectInputException("В запросе отсутсвует параметр " + neededParam );
                map.put(neededParam, Integer.valueOf(Arrays
                        .stream(paramsMap.get(neededParam))
                        .findFirst()
                        .orElseThrow(NotCorrectInputException::new)));
            });
            return map;
        } catch (NumberFormatException e) {
            throw new NotCorrectInputException("Некорректный заголовок/параметр в запросе");
        }
    }
}
