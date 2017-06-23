<%-- 
    Document   : developers
    Created on : 15 Jun, 2017, 12:43:34 PM
    Author     : Aditya
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<t:ui_layout title="Save2Drive - Developers">
    
    <jsp:attribute name="head_area">
        
    </jsp:attribute>
    
    <jsp:attribute name="body_area">
        <div class="mui-row">
            <div class="mui-col-xs-6 mui-col-md-offset-4 mui-col-md-4">
                <div class="mui--text-display2 mui--text-center">Project Developers</div>
            </div>
        </div>
        <br/>
        <div class="mui-row">
            <div class="mui-col-md-6" id = "dhaval-card">
                <div class ="image-hover-text-container mui--z4" id = "dhaval-content">
                    <div class="mui--text-display1 mui--text-center">Dhaval Mehta</div>
                    <div class="image-hover-image" align="center" style="margin-top:8%;">
                        <img src ="https://avatars0.githubusercontent.com/u/20968146?v=3&s=400" alt="Dhaval Mehta">
                    </div>
                    <article style="margin-top: 5%;">
                        <a href = "https://github.com/dhavalmehta1997" target="_blank">Dhaval</a>
                        is a final-year Computer Engineering student at Dharmsinh Desai University, Nadiad. He is the guy for the backend of this app. He is a competitive programmer & loves development in Java & found new love for Ruby
                    </article>
                </div>
            </div>
            <div class="mui-col-md-6" id = "aditya-card">
                <div class ="image-hover-text-container mui--z4" id = "aditya-content">
                    <div class="mui--text-display1 mui--text-center">Aditya Krishnakumar</div>
                    <div class="image-hover-image" align="center" style="margin-top:8%;">
                        <img src ="https://avatars2.githubusercontent.com/u/15857647?v=3&s=460" alt="Aditya Krishnakumar">
                    </div>
                    <article style="margin-top: 5%;">
                        <a href = "https://github.com/beingadityak" target="_blank">Aditya</a>
                        is a final-year Computer Engineering student at Dharmsinh Desai University, Nadiad. He is responsible for developing the front-end of this app. He loves development in JavaScript, HTML & CSS. He has also started to learn Python.
                    </article>
                </div>
            </div>
        </div>
        
    </jsp:attribute>
    
</t:ui_layout>
