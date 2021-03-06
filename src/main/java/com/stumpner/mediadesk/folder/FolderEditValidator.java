package com.stumpner.mediadesk.folder;

import org.springframework.validation.Validator;
import org.springframework.validation.Errors;

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
 * Date: 15.01.2013
 * Time: 18:25:33
 * To change this template use File | Settings | File Templates.
 */
public class FolderEditValidator implements Validator {

    public boolean supports(Class aClass) {
        return true;
    }

    public void validate(Object o, Errors errors) {

        FolderMultiLang folder = (FolderMultiLang)o;
        if (!NameValidator.validate(folder.getName())) {
            errors.rejectValue("name","edit.namenotvalid");
        }

        /*
        if (!NameValidator.validateQuotes(folder.getName())) {
            errors.rejectValue("catName","edit.namenotvalid");
        }

        if (!NameValidator.validateQuotes(folder.getTitleLng1())) {
            errors.rejectValue("titleLng1","edit.namenotvalid");
        }

        if (!NameValidator.validateQuotes(folder.getTitleLng2())) {
            errors.rejectValue("titleLng2","edit.namenotvalid");
        } */
    }
}
