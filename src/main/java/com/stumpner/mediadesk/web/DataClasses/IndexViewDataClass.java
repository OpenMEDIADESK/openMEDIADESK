package com.stumpner.mediadesk.web.DataClasses;

import com.stumpner.mediadesk.web.servlet.IDataClass;
import com.stumpner.mediadesk.core.Config;

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
 * User: franzstumpner
 * Date: 22.03.2005
 * Time: 22:13:00
 * To change this template use File | Settings | File Templates.
 */
public class IndexViewDataClass implements IDataClass {

    public String getSiteTitle() {
        return Config.webTitle;
    }

    public String getSiteKeywords() {
        return Config.webKeywords;
    }

    public String getSiteDescription() {
        return Config.webDescription;
    }
}
