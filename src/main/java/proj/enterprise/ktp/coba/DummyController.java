/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Controller.java to edit this template
 */
package proj.enterprise.ktp.coba;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.print.attribute.standard.Media;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import org.apache.catalina.connector.Response;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import proj.enterprise.ktp.Data;
import proj.enterprise.ktp.DataJpaController;

/**
 *
 * @author FADHIL
 */
@Controller
public class DummyController {
    DummyJpaController dummyController = new DummyJpaController();
    List<Dummy> data = new ArrayList<>();
    
    @RequestMapping("/read")
    @ResponseBody
    public List<Dummy> getDummy(){
        try {
            data = dummyController.findDummyEntities();
        }
        catch (Exception e){}
        return data;
    }
    
    @RequestMapping("/create")
    public String createDummy(){
        return "dummy/create";
    }
    
    @PostMapping(value="/newdata", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public String newDummy(@RequestParam("file") MultipartFile file, HttpServletRequest data) throws ParseException, Exception{
        Dummy dumdata = new Dummy();
        
        String id = data.getParameter("id");
        int iid = Integer.parseInt(id);
        
        String tanggal = data.getParameter("");
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(tanggal);
        
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        byte[] image = file.getBytes();
        
        dumdata.setId(iid);
        dumdata.setTanggal(date);
        dumdata.setGambar(image);
        
        dummyController.create(dumdata);
        
        return "created";
    }
}
