/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package computacionfim.conversorhorarios;

import java.io.*;
import java.util.*;
import java.time.LocalDateTime;
//import com.aspose.cells.*;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ControlConversor {

    private int MaxCharLinea = 132;
    private int MaxCharHora = 8;
    private int MaxCharDesc = 70;
    private int MaxLineaHeader = 13;
    private int MaxLineaMateria = 3;
    private int MaxLineaHoja = 66;
    public ArrayList<String> lineas = new ArrayList<>();
    public ArrayList<Materia> materias=new ArrayList<>();
    
//ES UN GRUPO POR HOJA

//MARCO Antonio Reyna Vargas alumno de 9veno ya convirtio archivos con la subdirectora
//El Profe jorge ibarra tambien puede saber de esto.
    public ControlConversor() {
    }


    public boolean leerXLS(File file) {
        
        //VERIFICAR EXTENSION DEL ARCHIVO
        String[] nombreExtension = file.getName().split("\\.");
        if (!(nombreExtension[1].equalsIgnoreCase("xlsx")||nombreExtension[1].equalsIgnoreCase("xls"))) {
            return false;
        }
        System.out.println(">> Formato correcto");
        try {
            FileInputStream fileStream = new FileInputStream(file);
            Workbook libroExcel = new XSSFWorkbook(fileStream);
            XSSFSheet hojaExcel = (XSSFSheet) libroExcel.getSheetAt(0);
            Iterator<Row> lineaIterator = hojaExcel.iterator();

            while(lineaIterator.hasNext())
//            for (int i = 0; i < 10; i++)
            {
                Materia lineaMateria=new Materia();
                Row linea = lineaIterator.next();
                Iterator<Cell> celdaIterator = linea.cellIterator();
                while (celdaIterator.hasNext()) {
                    Cell celda = celdaIterator.next();
                    lineaMateria.llenarCelda(celda.getStringCellValue().trim());
                    //System.out.print(celda.getStringCellValue()+"\t");
                }
                lineaMateria.llenarMateria();
                materias.add(lineaMateria);
                //System.out.println("");
            }
            materias.remove(0);
            verMaterias();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ControlConversor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ControlConversor.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }
    public void verMaterias(){
        System.out.println("Cantidad de Materias:"+materias.size());
        for (int i = 0; i < materias.size()-1; i++) {
            System.out.println(materias.get(i));
        }
    }

    public boolean leerTxt(String path, String name, File file) {
        //VERIFICAR EXTENSION DEL ARCHIVO
        String[] nombreExtension = name.split("\\.");
        if (!nombreExtension[1].equalsIgnoreCase("txt")) {
            return false;
        }
        System.out.println(">> Formato correcto");

        //LEER ARCHIVO
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!(linea.isBlank() || linea.isEmpty())) {
                    lineas.add(linea);
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println(">> Error en lectura archivo");
        } catch (IOException ex) {
            System.out.println(">> Error en lectura linea");
        }

        imprimirLineas();
        SepararLineas();
        return true;
    }

    public void imprimirLineas() {
        System.out.println(lineas.size());
        for (int i = 0; i < lineas.size(); i++) {
            System.out.println(lineas.get(i));
        }
    }

    public void SepararLineas() {
        for (int i = 0; i < 10; i++) {
            String materia = lineas.get(i).trim();
            String[] columnas = materia.split("[ ]{2,}");
            System.out.println(columnas[0] + "\t");

        }

    }
}
