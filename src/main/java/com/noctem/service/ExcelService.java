package com.noctem.service;

import com.noctem.domain.UserGroup;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Gustavo on 10/11/16.
 */
@Service
public class ExcelService {


    public void writeExcel(Set<UserGroup> userGroupSet) throws IOException {
        HSSFWorkbook libro = new HSSFWorkbook();
        HSSFSheet hoja = libro.createSheet();
        int i=0;
        for (Iterator<UserGroup> it = userGroupSet.iterator(); it.hasNext(); ) {
            UserGroup userGroup = it.next();
            Row row = hoja.createRow(i);
            row.createCell(0) .setCellValue(userGroup.getName());
            row.createCell(1) .setCellValue(userGroup.getEmail());
            i++;
        }
        FileOutputStream elFichero = new FileOutputStream("lista.xls");
        libro.write(elFichero);
    }


}
