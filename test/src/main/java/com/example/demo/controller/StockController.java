package com.example.demo.controller;

import com.example.demo.model.StockInfo;
import com.example.demo.model.StockType;
import com.example.demo.model.userinfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class StockController {

    @Autowired
    JdbcTemplate jdbc;

    @RequestMapping("/addstock")
    public String addStock(String stockname, String stocktype, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String id = (String)session.getAttribute("uid");
        int i = jdbc.update("insert into goods values(null,?,?,?)", new Object[]{stockname, stocktype,id});
        int j = jdbc.update("insert into stock_info values(null,?,0)", stockname);
        
        
        if (i>0 && j>0) {
            model.addAttribute("result", "ok");
        } else {
            model.addAttribute("result", "failed");
        }

        return "info";
    }
   /* @RequestMapping("/addpeople")
    public String addpeople(String id,String pname,Model model,HttpServletRequest request){
        HttpSession session = request.getSession();
        Strind id = (String)session.getAttribute("pid");
        int i = jdbc.update("insert into goods values(null,?,?,?)", new Object[]{stockname, stocktype,id});
        int j = jdbc.update("insert into stock_info values(null,?,0)", stockname);
    }
*/
    @RequestMapping("/showstock.html")
    public String showStock(Model model) {
        List<StockInfo> info = jdbc.query("select goods.good_name, stock_type.type_name, stock_info.count from goods, stock_type, stock_info where goods.good_type = stock_type.id and goods.good_name = stock_info.good_name ", new BeanPropertyRowMapper<StockInfo>(StockInfo.class));
        model.addAttribute("info", info);

        return "showstock";
    }

    @RequestMapping("/seewho.html")
    public String seewho(Model model){
        List<StockInfo> info = jdbc.query(" select * from goods ", new BeanPropertyRowMapper<StockInfo>(StockInfo.class));
        model.addAttribute("info", info);
        return "seewho";
    }
   // select good_name,uname from goods; 

    @RequestMapping("/dropdom")
    public String dropdom(String good_name, Model model) {
        int i = jdbc.update("delete from goods where good_name =?", good_name);
        int j = jdbc.update("delete from stock_info where good_name =?", good_name);
        if(i ==1 && j == 1) {
            model.addAttribute("result", "成功！");
        } else {
            model.addAttribute("result", "失败！");            
        }
        return "info";
    }


    @RequestMapping("/drop")
    
        public String drop(String type_name, Model model) {
            int i = jdbc.update("delete from stock_type where type_name = ? ", type_name);
            if(i ==1 ) {
                model.addAttribute("result", "成功！");
            } else {
                model.addAttribute("result", "失败！");            
            }
            return "info";
        
    }

    @RequestMapping("/showuser.html")
    public String showuser(Model model){
        List<userinfo> info = jdbc.query("select * from users", new BeanPropertyRowMapper<userinfo>(userinfo.class));
        model.addAttribute("info", info);
        return "showuser";
    }

    @RequestMapping("/update")
    public String update( String aa, Model model) {
        int count = jdbc.queryForObject("select count from stock_info where good_name=?", Integer.class , aa);
        model.addAttribute("count", count);
        model.addAttribute("name", aa);
        return "updatestock";
    }

    @RequestMapping("/updatestock")
    public String updateStock(String goodname, String count, Model model) {
        int i = jdbc.update("update stock_info set count = ? where good_name=?", new Object[]{count,goodname});
        if(i>0) {
            model.addAttribute("result", "ok");
        } else {
            model.addAttribute("result", "fail");
        }
        return "info";
    }

    @RequestMapping("/updatedom")
    public String updom(String goodname, String count, Model model) {
        int i = jdbc.update("update stock_info good_name = ? where good_name=?", new Object[]{count,goodname});
        if(i>0) {
            model.addAttribute("result", "ok");
        } else {
            model.addAttribute("result", "fail");
        }
        return "info";
    }

    @RequestMapping("/addstocktype")
    public String addStockType(String type_name, Model model) {
        int i = jdbc.update("insert into stock_type values(null, ?) ", type_name);
        if(i ==1 ) {
            model.addAttribute("result", "添加成功！");
        } else {
            model.addAttribute("result", "添加失败！");            
        }
        return "info";
    }
    @RequestMapping("/addstock.html")
    public String addStockHtml(Model model) {
        List<StockType> stockTypes = jdbc.query("select * from stock_type ", new BeanPropertyRowMapper<StockType>(StockType.class));
        model.addAttribute("stockTypes", stockTypes);


        return "addstock";
    }



    @RequestMapping("/showuser_user.html")
    public String showuser_userHtml(Model model){
        return "showuser_user";
    }
    @RequestMapping("/addtype_user.html")
    public String addtype_userHtml(Model model){
        return "addtype_user";
    }
}