<%-- 
    Document   : about
    Created on : 15 Jun, 2017, 12:43:14 PM
    Author     : Aditya
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<t:ui_layout title="Save2Drive - About">
    
    <jsp:attribute name="head_area">
        
    </jsp:attribute>
    
    <jsp:attribute name="body_area">
        <div class="mui-row">
            <div class="mui-col-xs-6 mui-col-md-offset-4 mui-col-md-4">
                <div class="mui--text-display2 mui--text-center">About</div>
            </div>
        </div>
        <br/>
        <div id="about-content">
            <div class="mui-row">
                <article>
                    With this app, you shall be able to download a file from URL <b>directly</b> to Google Drive.
                    Tired of saving downloads to your PC? One solution is to save them on a cloud storage like Google Drive or Dropbox.
                    But, what about uploading them to your drive? You would waste your bandwidth for 2 operations - Download & Upload.
                    We understand your concern. So, we created <b><em>Save2Drive</em></b>. A simple webapp solution for saving your bandwidth!
                </article>
                <article>You just have to follow these steps :
                    <ol>
                        <li><a href="/OAuth2/OAuthRedirectServlet">Authorize our app</a> to let us upload the file</li>
                        <li>Next, paste the download URL of the file</li>
                        <li>Then relax, while we save your file to Google Drive</li>
                    </ol>
                </article>
            </div>
        </div>
        
    </jsp:attribute>
    
</t:ui_layout>