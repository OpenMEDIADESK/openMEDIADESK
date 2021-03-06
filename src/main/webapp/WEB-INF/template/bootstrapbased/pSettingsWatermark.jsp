<%@ page import="com.stumpner.mediadesk.core.Config"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@page contentType="text/html;charset=utf-8" language="java" %>

<jsp:include page="header.jsp"/>

<!-- spalte2 -->
<div class="col-sm-10 main"> <!-- col-sm-10 main SPALTE 2 FÜR INHALT -->
<!-- ###################################################################################################################################################### -->
<!-- HIER STARTET DER INHALTSBEREICH ###################################################################################################################### -->
<!-- AB HIER GANZ NEU MIT NEUER EINTEILUNG !!! ############################################################################################################ -->
<!-- breadcrumbs -->
<ol class="breadcrumb">
	<li><a href="<c:url value="${home}"/>"><i class="fa fa-folder-o fa-fw"></i> Home</a></li>
    <li><a href="<c:url value="/${lng}/settings"/>"><i class="fa fa-sliders fa-fw"></i> <spring:message code="menu.settings"/></a></li>
    <li class="active"><i class="fa fa-certificate"></i> <spring:message code="set.watermark"/></li>
</ol>
<!-- /breadcrumbs -->
<!-- ordnertitel und infos -->
<h3><spring:message code="set.watermark"/></h3>
<h4><spring:message code="set.headline"/></h4>
<!-- /ordnertitel und infos -->
<!-- mediadesk abstand -->
<div class="md-space">&nbsp;</div>
<!-- /mediadesk abstand -->
<!-- ###################################################################################################################################################### -->


<div class="row">
	<div class="col-sm-12">
	<!-- FORMS FÜR EDIT -->


    <form method="post" action="<c:url value="setwatermark"/>">
    <input type="hidden" name="JSESSIONID" value="<c:out value="${pageContext.request.session.id}"/>"/>

    <spring:bind path="command.intensity">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="text<c:out value="${status.expression}"/>"><spring:message code="set.watermark.intensity"/></label>
        <input type="text" class="form-control input-sm" id="text<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${status.error}"> aria-describedby="eingabefeldFehler2Status"</c:if>>
        <c:if test="${status.error}">
          <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
          <span id="eingabefeldFehler2Status" class="sr-only">(Fehler)</span>
        </c:if>
        </div>
    </spring:bind>

    <spring:bind path="command.gravity">
        <div class="form-group<c:if test="${status.error}"> has-error has-feedback</c:if>">
        <label for="input<c:out value="${status.expression}"/>"><spring:message code="set.watermark.gravity"/></label>
            <select name="<c:out value="${status.expression}"/>" id="input<c:out value="${status.expression}"/>" class="form-control">
                <option value="1"<c:if test="${status.value==1}"> selected</c:if>><spring:message code="set.watermark.gravity.nw"/></option>
                <option value="2"<c:if test="${status.value==2}"> selected</c:if>><spring:message code="set.watermark.gravity.n"/></option>
                <option value="3"<c:if test="${status.value==3}"> selected</c:if>><spring:message code="set.watermark.gravity.ne"/></option>
                <option value="4"<c:if test="${status.value==4}"> selected</c:if>><spring:message code="set.watermark.gravity.w"/></option>
                <option value="5"<c:if test="${status.value==5}"> selected</c:if>><spring:message code="set.watermark.gravity.c"/></option>
                <option value="6"<c:if test="${status.value==6}"> selected</c:if>><spring:message code="set.watermark.gravity.e"/></option>
                <option value="7"<c:if test="${status.value==7}"> selected</c:if>><spring:message code="set.watermark.gravity.sw"/></option>
                <option value="8"<c:if test="${status.value==8}"> selected</c:if>><spring:message code="set.watermark.gravity.s"/></option>
                <option value="9"<c:if test="${status.value==9}"> selected</c:if>><spring:message code="set.watermark.gravity.se"/></option>
            </select>
        </div>
    </spring:bind>

    <button type="submit" name="watermark" class="btn btn-default"><spring:message code="set.watermark.file"/></button>
    <button type="submit" name="Submit" class="btn btn-default"><spring:message code="mediaedit.submit"/></button>

    </form>

    <spring:hasBindErrors name="command">
    <div class="alert alert-danger" role="alert">
      <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
      <span class="sr-only">Fehler</span>
            <spring:bind path="command">
                <div class="formErrorSummary">
                <c:forEach items="${status.errorMessages}" var="error">
                    <c:out value="${error}"/><br>
                </c:forEach>
                </div>
            </spring:bind>
    </div>
    </spring:hasBindErrors>

    <!-- /FORMS FÜR EDIT -->
	</div>
</div>      <!-- /row -->
<!-- ENDE EDIT BILD -->
<!-- ###################################################################################################################################################### -->



<!-- mediadesk abstand -->
<div class="md-space-lg">&nbsp;</div>
<!-- /mediadesk abstand -->


	</div> <!-- /col-sm-10 main SPALTE 2 FÜR INHALT ZU -->
<!-- ###################################################################################################################################################### -->
<!-- HIER ENDE INHALTSBEREICH ############################################################################################################################# -->

<jsp:include page="footer.jsp"/>