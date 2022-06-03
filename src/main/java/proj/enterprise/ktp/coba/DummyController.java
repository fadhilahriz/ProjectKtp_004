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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import proj.enterprise.ktp.Data;
import proj.enterprise.ktp.DataJpaController;
import static proj.enterprise.ktp.coba.Dummy_.id;
import proj.enterprise.ktp.coba.exceptions.NonexistentEntityException;

/**
 *
 * @author FADHIL
 */
@Controller
public class DummyController {
    DummyJpaController dummyController = new DummyJpaController();
    List<Dummy> data = new ArrayList<>();
    
    @RequestMapping("/read")
    //@ResponseBody
    public String getDummy (Model model) {
    int record = dummyController.getDummyCount();
        String result = "";
        
        try{
            data = dummyController.findDummyEntities().subList(0, record);
        }
        catch (Exception e){
            result=e.getMessage();
        }
        
        model.addAttribute("godummy", data);
        model.addAttribute("record", record);
         
        return "dummy";    
    }
    
    @RequestMapping("/create")
    public String createDummy(){
        return "dummy/create";
    }
    
    @PostMapping(value="/newdata", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    //@ResponseBody
    public String newDummy(HttpServletRequest data, @RequestParam("gambar") MultipartFile file) throws ParseException, Exception{
        Dummy dumdata = new Dummy();
        
        String id = data.getParameter("id");
        int iid = Integer.parseInt(id);
        
        String tanggal = data.getParameter("tanggal");
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(tanggal);
        
//        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        byte[] image = file.getBytes();
        
        dumdata.setId(iid);
        dumdata.setTanggal(date);
        dumdata.setGambar(image);
        
        dummyController.create(dumdata);
        
        return "dummy/create";
    }
    
    @RequestMapping (value="/image" , method = RequestMethod.GET ,produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public ResponseEntity<byte[]> getImg(@RequestParam("id") int id) throws Exception {
	Dummy dumdata = dummyController.findDummy(id);
	byte[] image = dumdata.getGambar();
	return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(image);
    }
    
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String deleteDummy(@PathVariable("id") int id) throws Exception {
	dummyController.destroy(id);
	return "redirect:/read";
    }
    
    @PostMapping(value="/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    //@ResponseBody
    public String updateDummy(HttpServletRequest data, @RequestParam("gambar") MultipartFile file) throws ParseException, Exception{
        Dummy dumdata = new Dummy();
        
        String id = data.getParameter("id");
        int iid = Integer.parseInt(id);
        dumdata = dummyController.findDummy(iid);
        
        String tanggal = data.getParameter("tanggal");
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(tanggal);
        
//        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        
        
        dumdata.setId(iid);
        dumdata.setTanggal(date);
        if(!file.isEmpty()){
            byte[] image = file.getBytes();
            dumdata.setGambar(image);
        
            dummyController.edit(dumdata);

            return "redirect:/read";
        }else{
            dummyController.edit(dumdata);

            return "redirect:/read";
        }
        
    }
    
    @RequestMapping("/edit/{id}")
    public String editDummy(Model model, @PathVariable("id") int id) throws Exception {
//        try{
//            data = dummyController.findDummyEntities();
//        }
//        catch (Exception e){}
      
        Dummy data = dummyController.findDummy(id);
        model.addAttribute("godummy", data);
        return "dummy/update";
    }
    
//    @RequestMapping(value = "update/{id}", method = RequestMethod.POST)
//    public String updateDummy(@ModelAttribute("id") Dummy dummy ) throws Exception {
//        dummyController.edit((Dummy) id);
//        return "redirect:/read";
//    }
}
