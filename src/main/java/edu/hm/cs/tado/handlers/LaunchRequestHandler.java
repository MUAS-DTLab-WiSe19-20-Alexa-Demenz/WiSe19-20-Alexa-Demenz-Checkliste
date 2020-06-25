/*
     Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package edu.hm.cs.tado.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.response.ResponseBuilder;
import edu.hm.cs.tado.PhrasesAndConstants;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.requestType;

public class LaunchRequestHandler implements RequestHandler {


    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(requestType(LaunchRequest.class));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        ResponseBuilder responseBuilder = input.getResponseBuilder();

        //see if list is stored already
        AttributesManager attributesManager = input.getAttributesManager();
        Map<String, Object> persistentAttributes = attributesManager.getPersistentAttributes();

        if (!persistentAttributes.containsKey("lists")) {
            persistentAttributes.put("lists", new ArrayList());
            attributesManager.savePersistentAttributes();
        }
        responseBuilder.withSimpleCard(PhrasesAndConstants.CARD_TITLE, PhrasesAndConstants.WELCOME)
                .withSpeech(PhrasesAndConstants.WELCOME)
                .withShouldEndSession(false);
        return responseBuilder.build();
    }
}