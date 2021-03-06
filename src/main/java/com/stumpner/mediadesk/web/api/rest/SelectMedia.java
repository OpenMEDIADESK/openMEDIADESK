package com.stumpner.mediadesk.web.api.rest;

import com.stumpner.mediadesk.core.service.MediaObjectService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.media.MediaObject;
import org.springframework.web.bind.MissingServletRequestParameterException;

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
 * SelectMedia-Servlet um ein medienobjekt auszuwählen
 * /api/rest/selectMedia/{true|false}/{mediaId}/{folderId}
 * /api/rest/selectMedia/true/5523/7
 *
 *   1   2    3            4   5   6
 *
 * Beispiel:
 * /api/rest/selectMedia/false/ löscht bzw. leert die ausgewählten Medienobjekte
 *
 * Parameter:
 * 1 = true|false ob aus oder abgewählt wird,
 * 2 = ID des Medienobjekts das ab/angewählt wird
 * 3 = (Optional) Kategorie-ID von der aus ausgewählt wird (wichtig für verschieben)
 */
public class SelectMedia extends RestBaseServlet {

    private void selectMedia(HttpServletRequest request, HttpServletResponse response) throws ServletException, ObjectNotFoundException {

        if (true) {
            //System.out.println("REST-API-CALL: "+request.getRequestURI());
        }

        if (request.getRequestURI().startsWith("/api/rest/selectMedia/")) {


            if (getUriSectionCount(request)<5) {
                if (getUriSectionCount(request)==4) {
                    //Liste der gew�hlten Medien ausgeben
                    jsonSelectedMediaList(request, response);
                } else {
                    throw new MissingServletRequestParameterException("Fehlende Parameter","/api/rest/selectMedia/{true|false}/{mediaId}");
                }
            } else {
                Boolean select = getUriSectionBoolean(4, request);
                Integer mediaId = getUriSectionInt(5, request);
                Integer folderId = getUriSectionInt(6, request);
                if (mediaId==null) {
                    //Kein medienobjekt angegeben - dann alle abw�hlen
                    List<MediaObject> selectedMediaList = MediaObjectService.getSelectedMediaObjectList(request.getSession());

                    MediaObjectService.deselectMedia(null, request);

                } else {
                    if (select){
                        if (!MediaObjectService.selectMedia(
                                mediaId,
                                folderId,
                                request)) {
                            throw new ObjectNotFoundException();
                        }
                    } else {
                        MediaObjectService.deselectMedia(
                                mediaId,
                                request);
                    }

                }
            }
        } else {
            try {
                response.sendError(404, "Syntax Error in API-Call: /api/rest/selectMedia/{true|false}/{mediaId}/{folderId}");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private void jsonSelectedMediaList(HttpServletRequest request, HttpServletResponse response) {

        List<MediaObject> selectedMediaList = MediaObjectService.getSelectedMediaObjectList(request.getSession());
        try {
            PrintWriter out = response.getWriter();
            out.print("[");
            for (MediaObject mediaObject : selectedMediaList) {
                out.print("\n {\"ivid\": "+mediaObject.getIvid()+"}");

                if (selectedMediaList.indexOf(mediaObject)<selectedMediaList.size()-1) {
                    out.print(",");
                } else {
                    //Letztes Element
                }
            }
            out.println("\n]");
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            selectMedia(request, response);
        } catch (ObjectNotFoundException e) {
            response.sendError(404, "BasicMediaObject not found");
        } catch (MissingServletRequestParameterException e) {
            response.sendError(404, "BasicMediaObject not found");
        }

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            selectMedia(request, response);
        } catch (ObjectNotFoundException e) {
            response.sendError(404, "BasicMediaObject not found");
        } catch (MissingServletRequestParameterException e) {
            response.sendError(404, "BasicMediaObject not found");
        }
    }
}
