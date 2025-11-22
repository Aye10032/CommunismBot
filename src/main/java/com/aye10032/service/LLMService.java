package com.aye10032.service;

public interface LLMService {

    /**
     * Invoke chat model with Spring AI.
     *
     * @param prompt user input content
     * @return model reply content
     */
    String chat(String prompt);

}
