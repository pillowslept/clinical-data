package co.edu.itm.clinicaldata.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import co.edu.itm.clinicaldata.dto.ResourcesWrapper;
import co.edu.itm.clinicaldata.exception.ValidateException;
import co.edu.itm.clinicaldata.service.FileService;

@Controller
@RequestMapping("/api/file")
public class FileController {

    @Autowired
    FileService fileService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity<String> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("investigatorId") Long investigatorId,
            @ModelAttribute("resources") ResourcesWrapper resources)
            throws ValidateException {
        return new ResponseEntity<String>(fileService.upload(file,
                investigatorId, resources), HttpStatus.OK);
    }

}