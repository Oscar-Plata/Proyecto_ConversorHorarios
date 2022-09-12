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
    //TAMAÑOS DELA CUADRICULA 
    final int MaxCharLinea = 132;
    final int MaxCharHora = 8;
    final int MaxCharDesc = 70;
    final int MaxLineaHeader = 13;
    final int MaxLineaMateria = 3;
    final int MaxLineaHoja = 66;
    //ALMACENAMIENTO
    public ArrayList<String> lineas = new ArrayList<>();    //para formato en txt (no se usa BORRAR)
    public ArrayList<Materia> materias = new ArrayList<>();
    //CONTROL DE CONVERSION
    private int numPagActual=0;
    private int grupoActual;
    private int materiaActual;
    
    /*Notas
    ES UN GRUPO POR HOJA
    MARCO Antonio Reyna Vargas alumno de 9veno ya convirtio archivos con la subdirectora
    El Profe jorge ibarra tambien puede saber de esto.
    */
    
    public ControlConversor() {
    }

    public boolean leerXLS(File file) {

        //VERIFICAR EXTENSION DEL ARCHIVO
        String[] nombreExtension = file.getName().split("\\.");
        if (!(nombreExtension[1].equalsIgnoreCase("xlsx") || nombreExtension[1].equalsIgnoreCase("xls"))) {
            return false;
        }
        System.out.println(">> Formato correcto");
        //LEER ARCHIVO DE EXCEL
        try {
            FileInputStream fileStream = new FileInputStream(file);
            Workbook libroExcel = new XSSFWorkbook(fileStream);
            XSSFSheet hojaExcel = (XSSFSheet) libroExcel.getSheetAt(0);
            Iterator<Row> lineaIterator = hojaExcel.iterator();
            //RECORRER ARCHIVO
            //            for (int i = 0; i < 10; i++)
            while (lineaIterator.hasNext()) {
                Materia lineaMateria = new Materia();
                Row linea = lineaIterator.next();
                Iterator<Cell> celdaIterator = linea.cellIterator();
                while (celdaIterator.hasNext()) {
                    Cell celda = celdaIterator.next();
                    lineaMateria.llenarCelda(celda.getStringCellValue().trim());    //Almacena la info en una celda de materia
                }
                lineaMateria.llenarMateria();                                       //Llena los campos de informacion con las celdas
                materias.add(lineaMateria);                                         //Agrega La materia a la coleccion
            }
            materias.remove(0);                                                     //Elimina el encabezado de la tabla de excel
            verMaterias();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ControlConversor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ControlConversor.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }

    public void verMaterias() {
        System.out.println("Cantidad de Materias:" + materias.size());
        for (int i = 0; i < materias.size() - 1; i++) {
            System.out.println(materias.get(i));
        }
    }
    
    public String crearEncabezado(){
        
        return "Taco";
    }
    //Escribir una linea punteada en todo el renglon
    public String linea(){
        return " ----------------------------------------------------------------------------------------------------------------------------------";
    }
    
    //Obtener la Hora Actual
    public String hora(){
        LocalDateTime ahora = LocalDateTime.now();
        return String.valueOf(ahora.getHour())+":"+String.valueOf(ahora.getMinute())+":"+String.valueOf(ahora.getSecond());
    }
    //Obtener la Fecha Actual
    public String fecha(){
        LocalDateTime ahora = LocalDateTime.now();
        String fechaStr=ahora.getDayOfMonth()+"/";
        switch(ahora.getMonthValue()){
            default:
            case 1: fechaStr+="Ene"; break;
            case 2: fechaStr+="Feb";break;
            case 3: fechaStr+="Mar";break;
            case 4: fechaStr+="Abr";break;
            case 5: fechaStr+="May";break;
            case 6: fechaStr+="Jun";break;
            case 7: fechaStr+="Jul";break;
            case 8: fechaStr+="Ago";break;
            case 9: fechaStr+="Sep";break;
            case 10: fechaStr+="Oct";break;
            case 11: fechaStr+="Nov";break;
            case 12: fechaStr+="Dic";break;
        }
        fechaStr+="/"+ahora.getYear();
        return fechaStr;
    }
    
    //Para formatos en TXT ya no se usa
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
