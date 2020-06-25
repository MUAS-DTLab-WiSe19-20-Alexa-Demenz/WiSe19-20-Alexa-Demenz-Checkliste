/*
     Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package edu.hm.cs.tado;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import edu.hm.cs.tado.handlers.*;
import edu.hm.cs.tado.service.MailSenderService;

import java.io.IOException;

public class TaDoStreamHandler extends SkillStreamHandler {

    private static Skill getSkill() throws IOException {
        return Skills.standard()
                .addRequestHandlers(
                        new CreateListIntentHandler(),
                        new DeleteListIntentHandler(),
                        new AddToListIntentHandler(),
                        new DeleteNamedElementFromListIntentHandler(),
                        new CheckElementIntentHandler(),
                        new WhatsOnThisListIntentHandler(),
                        new WhatsFinishedOnThisListIntentHandler(),
                        new SendListMailIntentHandler(new MailSenderService()),
                        new LaunchRequestHandler(),
                        new CancelandStopIntentHandler(),
                        new SessionEndedRequestHandler(),
                        new WhatsToDoOnThisListIntentHandler(),
                        new HelpIntentHandler(),
                        new NoListIntentHandler())
                .withTableName("TaDoList")
                .withAutoCreateTable(true)
                // Add your skill id below
                //.withSkillId("")
                .build();
    }

    public TaDoStreamHandler() throws IOException {
        super(getSkill());
    }

}
