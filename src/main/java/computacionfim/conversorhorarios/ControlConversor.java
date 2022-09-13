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
    final int MaxMateriaHoja = 10;
    //ALMACENAMIENTO
    public ArrayList<String> lineas = new ArrayList<>();    //para formato en txt (no se usa BORRAR)
    public ArrayList<Materia> materias = new ArrayList<>();
    //CONTROL DE CONVERSION
    private int numPagActual = 0;
    private int grupoActual=000;
    private String planActual;
    private String carreraActual;
    private int numCarreraActual;
    private int numMateriaActual;
    private Materia materiaActual;

    /*Notas
    ES UN GRUPO POR HOJA
    MARCO Antonio Reyna Vargas alumno de 9veno ya convirtio archivos con la subdirectora
    El Profe jorge ibarra tambien puede saber de esto.
     */
    public ControlConversor() {
    }

    public boolean leerXLS(File file) {
        materias.clear();
        lineas.clear();
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
            //verMaterias();
            convertir();

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
            crearEncabezado();
            System.out.println(materias.get(i));
        }
        
    }
    
    public void convertir(){
        String[] lineasEncAux= new String[13];
        materiaActual=materias.get(0);
        
        numPagActual=1;
        grupoActual=Integer.valueOf(materiaActual.getGrupo());
        planActual=materiaActual.getPlanEstudios();
        carreraActual=materiaActual.getNombrePE();
        numCarreraActual=Integer.valueOf(materiaActual.getNumeroPE());
        numMateriaActual=0;
        
        int contadorMateriasHoja=1;
        lineasEncAux=crearEncabezado();
        
        System.out.println(materiaActual);
        
        for (int i = 1; i < materias.size()-1; i++) {
            numMateriaActual=i;
            materiaActual=materias.get(numMateriaActual);
            
            if(Integer.valueOf(materiaActual.getGrupo())==grupoActual){
                System.out.println(materiaActual);
                contadorMateriasHoja++;
                if(contadorMateriasHoja>MaxMateriaHoja){
                    //salto de hoja
                    numPagActual++;
                    crearEncabezado();
                    System.out.println(materiaActual);
                    contadorMateriasHoja=1;
                }
            }else{
                //salto de hoja
                numPagActual++;
                grupoActual=Integer.valueOf(materiaActual.getGrupo());
                planActual=materiaActual.getPlanEstudios();
                carreraActual=materiaActual.getNombrePE();
                numCarreraActual=Integer.valueOf(materiaActual.getNumeroPE());
                contadorMateriasHoja=1;
                crearEncabezado();
                System.out.println(materiaActual);
            }
        }
    }

    public String[] crearEncabezado() {
        String[] encabezado = new String[MaxLineaHeader];
        encabezado[0] = "\n";
        encabezado[1] = " " + hora() + "                                    UNIVERSIDAD AUTONOMA DE BAJA CALIFORNIA                                    " + fecha() + "\n";
        encabezado[2] = "                                               COORDINACION GENERAL DE RECURSOS HUMANOS\n";
        String paginaFTM=String.format("%3d", numPagActual);
        encabezado[3] = " RPLAN005                                           CUADRICULA DE HORARIOS                                                PAG.  " + paginaFTM + "\n";
        encabezado[4] = "                                                        Periodo :" + periodoActual() + "\n";
        encabezado[5] = "\n";
        String grupoFTM=String.format("%03d", grupoActual);
        encabezado[6] = " UNIDAD ACADEMICA: 105 FACULTAD DE INGENIERIA MXLI                                                               GRUPO: '" + grupoFTM + "' '   '"+"\n";
        String carreraFTM=String.format("%03d", numCarreraActual);
        encabezado[7] = " CARRERA: "+carreraFTM+"          "+carreraActual+"\n";
        encabezado[8]=" PLAN DE ESTUDIOS: "+planActual+"\n";
        encabezado[9]=linea()+"\n";
        encabezado[10]=" CVE.ASIGNAT.       D E S C R I P C I O N                 CAP TPO     LUNES   MARTES  MIERCO  JUEVES  VIERNES  SABADO  DOMINGO  E/S\n";
        encabezado[11]=" No.CONTROL             M A E S T R O          EDIF SALON ASG SGP\n";
        encabezado[12]=linea()+"\n";
        for (int i = 0; i < MaxLineaHeader; i++) {
            System.out.print(encabezado[i]);
        }
        return encabezado;
    }
        public void crearMateria(){
        ArrayList<String> horas = new ArrayList<>();
        horas.add("07:00 - 18:00");
        horas.add("07:00 - 18:00");
        horas.add("07:00 - 18:00");
        horas.add("07:00 - 18:00");
        horas.add("07:00 - 18:00");
        horas.add("07:00 - 18:00");
        horas.add("07:00 - 18:00");
        for(int i = 0; i<horas.size();i++){
            horas.set(i,acondicionarHoras(horas.get(i)));
        }
        int numPE=33537;
        int claveAsig = 339;
        int numEmpleado = 031054;
        String nombreMateria = "Ecuaciones diferenciales";
        String nombreProfesor=  "URIARTE DE LA CRUZ PABLO JESUS";
        int edificio = 3;
        int salon = 505;
        int capacidad=40;
        char tipo = 'C';
        String subgrupo ="1";
        String [] textoMateria = new String[3];
        textoMateria[0] = espacios(69)+"|"+espacios(7)+"|"+espacios(7)+"|"+espacios(7)+"|"+espacios(7)+"|"+espacios(7)+"|"+espacios(7)+"|"+espacios(7)+"|\n";
        textoMateria[1] =espacios(6)+numPE+espacios(2)+nombreMateria;
        while(textoMateria[1].length()<40){
            textoMateria[1]+=" ";
        }
        textoMateria[1] = capacidad+espacios(2)+tipo+espacios(5)+"| "+horas.get(0).split("-")[0]+" | "+horas.get(1).split("-")[0]+" | "+horas.get(2).split("-")[0]+" | "+horas.get(3).split("-")[0]+" | "+horas.get(4).split("-")[0]+" | "+horas.get(5).split("-")[0]+" | "+horas.get(6).split("-")[0]+" | ";
        textoMateria[2] = " "+claveAsig+espacios(5)+numEmpleado+" "+nombreProfesor.substring(0,29)+" "+edificio+espacios(4)+salon + espacios(13) + "| "+horas.get(0).split("-")[1]+" | "+horas.get(1).split("-")[1]+" | "+horas.get(2).split("-")[1]+" | "+horas.get(3).split("-")[1]+" | "+horas.get(4).split("-")[1]+" | "+horas.get(5).split("-")[1]+" | "+horas.get(6).split("-")[1]+" | ";
        System.out.println(textoMateria[0]);
        System.out.println(textoMateria[1]);
                System.out.println(textoMateria[2]);
                System.out.println(linea());
    }
    public String acondicionarHoras(String hora){
        String primeraHora =hora.split("-")[0].trim();
        String segundaHora = hora.split("-")[1].trim();
        if(primeraHora.charAt(0)=='0'){
            primeraHora=" "+primeraHora.substring(1,4);
        }
        if(segundaHora.charAt(0)=='0'){
            segundaHora=" "+segundaHora.substring(1,4);
    }
        String horaGeneral = primeraHora+"-"+segundaHora;
        return horaGeneral;
        
    }
    public String acondicionarNombre(String nombre){
        return nombre.substring(0,29);
    }


    //Escribir una linea punteada en todo el renglon
    public String linea() {
        return " ----------------------------------------------------------------------------------------------------------------------------------";
    }

    public String espacios(int cantidad) {
        String auxiliar = "";
        for (int i = 0; i < cantidad; i++) {
            auxiliar += " ";
        }
        return auxiliar;
    }

    //Obtener la Hora Actual
    public String hora() {
        LocalDateTime ahora = LocalDateTime.now();
        return String.valueOf(ahora.getHour()) + ":" + String.valueOf(ahora.getMinute()) + ":" + String.valueOf(ahora.getSecond());
    }

    //Obtener la Fecha Actual
    public String fecha() {
        LocalDateTime ahora = LocalDateTime.now();
        String fechaStr = ahora.getDayOfMonth() + "/";
        switch (ahora.getMonthValue()) {
            default:
            case 1:
                fechaStr += "Ene";
                break;
            case 2:
                fechaStr += "Feb";
                break;
            case 3:
                fechaStr += "Mar";
                break;
            case 4:
                fechaStr += "Abr";
                break;
            case 5:
                fechaStr += "May";
                break;
            case 6:
                fechaStr += "Jun";
                break;
            case 7:
                fechaStr += "Jul";
                break;
            case 8:
                fechaStr += "Ago";
                break;
            case 9:
                fechaStr += "Sep";
                break;
            case 10:
                fechaStr += "Oct";
                break;
            case 11:
                fechaStr += "Nov";
                break;
            case 12:
                fechaStr += "Dic";
                break;
        }
        fechaStr += "/" + ahora.getYear();
        return fechaStr;
    }

    public String periodoActual() {
        LocalDateTime ahora = LocalDateTime.now();
        String periodo = ahora.getYear() + "-";
        if (ahora.getMonthValue() >= 6) {
            periodo += "2";
        } else {
            periodo += "1";
        }
        return periodo;
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
