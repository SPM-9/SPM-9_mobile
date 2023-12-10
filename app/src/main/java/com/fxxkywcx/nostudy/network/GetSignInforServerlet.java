//package com.fxxkywcx.nostudy.network;
//
//import jakarta.servlet.*;
//import jakarta.servlet.http.*;
//import jakarta.servlet.annotation.*;
//
//import java.io.IOException;
//
//@WebServlet(name = "GetSignInforServerlet", value = "/GetSignInforServerlet")
//public class GetSignInforServerlet extends HttpServlet {
//    private String hasSign;
//    private String isSignSuccess;
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        //查询数据库当前是否有签到或没签到
//
//        //有签到设置
//        if(){
//            hasSign="true";
//        }else if(){
//            hasSign="false";
//        }else {
//            hasSign="already";
//        }
//        //设置attribute
//
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        //查询数据库是否超过签到时间
//
//        //没超读取json,添加后更新数据库
//        if (){
//            isSignSuccess="true";
//
//
//        }else {//超过
//            isSignSuccess="false";
//        }
//
//
//
//    }
//}
