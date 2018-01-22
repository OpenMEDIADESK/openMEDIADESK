<%@ page import="com.stumpner.mediadesk.core.Config"%><%@ page import="com.stumpner.mediadesk.core.database.sc.FolderService"%><%@ page import="com.stumpner.mediadesk.image.folder.Folder"%><%@ page import="java.text.Format"%><%@ page import="java.text.DateFormat"%><%@ page import="java.text.SimpleDateFormat"%><%@ page import="com.stumpner.mediadesk.core.database.sc.CategoryService"%><%@ page import="com.stumpner.mediadesk.image.category.Category"%><%@ page import="java.util.*"%><%@ page import="com.stumpner.mediadesk.web.servlet.Sitemap" %><%@ page contentType="text/xml;charset=UTF-8" language="java" %><?xml version="1.0" encoding="UTF-8"?>
  <urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
<%

    //System.out.println("Sitemap Request");
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");


    //categories, geht derzeit nur in eine tiefe bis 20 categories

    CategoryService categoryService = new CategoryService();
    //List categoryList = categoryService.getCategorySubTree(0,20);
    List categoryList = categoryService.getAllCategoryListSuborder();
    Iterator categorys = categoryList.iterator();
    Calendar calenderNow = GregorianCalendar.getInstance();

    while (categorys.hasNext()) {
        Category category = (Category)categorys.next();
        Date changedDate = new Date();
        double prio = 0.5;
        String mod = "always";

        if (category.getChangedDate()!=null) {
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.setTime(category.getChangedDate());
            changedDate = category.getChangedDate()==null ? new Date() : category.getChangedDate();

            if (category.getImageCount()>12) {
                prio = 1.0;
            }
            if (category.getImageCount()==0) {
                prio = 0.1;
            }


            mod = "always";
            if (calenderNow.get(Calendar.YEAR)==calendar.get(Calendar.YEAR)) {
                mod = "yearly";
                if (calenderNow.get(Calendar.MONTH)==calendar.get(Calendar.MONTH)) {
                    mod = "monthly";
                    if (calenderNow.get(Calendar.WEEK_OF_YEAR)==calendar.get(Calendar.WEEK_OF_YEAR)) {
                        mod = "weekly";
                        if (calenderNow.get(Calendar.DAY_OF_YEAR)==calendar.get(Calendar.DAY_OF_YEAR)) {
                            mod = "daily";
                            if (calenderNow.get(Calendar.HOUR_OF_DAY)==calendar.get(Calendar.HOUR_OF_DAY)) {
                                mod = "hourly";
                            }
                        }
                    }
                }
            }

            System.out.println("Now: "+calenderNow.getTime()+" Cal: "+calendar.getTime()+" mod= "+mod+" cat= "+category.getCategoryId());
        } else {
            System.out.println("changed date == null! cat"+category.getCategoryId());
        }


%>
   <url>
    <loc>http://<%= request.getServerName() %>/de/cat?id=<%= category.getCategoryId() %></loc>
    <lastmod><%= df.format(changedDate) %></lastmod>
    <changefreq><%= mod %></changefreq>
    <priority><%= prio %></priority>
   </url>
<% if (Config.multiLang) { %>
   <url>
    <loc>http://<%= request.getServerName() %>/en/cat?id=<%= category.getCategoryId() %></loc>
    <lastmod><%= df.format(changedDate) %></lastmod>
    <changefreq><%= mod %></changefreq>
    <priority><%= prio %></priority>
   </url>
<% } %>
<%
    }

%>

  </urlset>