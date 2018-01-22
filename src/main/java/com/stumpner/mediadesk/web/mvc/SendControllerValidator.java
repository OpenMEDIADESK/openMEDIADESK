package com.stumpner.mediadesk.web.mvc;

import org.springframework.validation.Validator;
import org.springframework.validation.Errors;
import com.stumpner.mediadesk.web.mvc.commandclass.SendCommand;

/*********************************************************
 Copyright 2017 by Franz STUMPNER (franz@stumpner.com)

 openMEDIADESK is licensed under Apache License Version 2.0

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 *********************************************************/

/**
 * Created by IntelliJ IDEA.
 * User: franz.stumpner
 * Date: 24.07.2008
 * Time: 08:16:04
 * To change this template use File | Settings | File Templates.
 */
public class SendControllerValidator implements Validator {

    public boolean supports(Class aClass) {
        return (aClass.equals(SendCommand.class));
    }

    public void validate(Object o, Errors errors) {

        SendCommand sendCommand = (SendCommand)o;
        if (sendCommand.getRecipient().indexOf("@")<1) {
            errors.rejectValue("recipient",null,"Value Required");
        }
    }
}