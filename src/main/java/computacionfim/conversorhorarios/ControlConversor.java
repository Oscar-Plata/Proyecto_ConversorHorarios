/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package computacionfim.conversorhorarios;

import java.io.*;
import java.util.*;
import java.time.LocalDateTime;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ControlConversor {

    private File archivo;
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
    private int grupoActual = 000;
    private String planActual;
    private String carreraActual;
    private int numCarreraActual;
    private int numMateriaActual;
    private Materia materiaActual;
    private int copiasExistentes=1;
    // RESULTADOS
    private boolean estado;
    private String pathNew;
    private String nameNew;

    /*Notas
    ES UN GRUPO POR HOJA
    MARCO Antonio Reyna Vargas alumno de 9veno ya convirtio archivos con la subdirectora
    El Profe jorge ibarra tambien puede saber de esto.
     */
    public ControlConversor() {
    }
    
    public boolean getEstado(){
        return estado;
    }
    public String getPathNew(){
        return pathNew;
    }
    public String getNameNew(){
        return nameNew;
    }

    public boolean leerXLS(File file) {
        if(file==null){
            estado=false;
            return false;
        }
        archivo = file;
        materias.clear();
        lineas.clear();
        //VERIFICAR EXTENSION DEL ARCHIVO
        String[] nombreExtension = file.getName().split("\\.");
        if (!(nombreExtension[1].equalsIgnoreCase("xlsx") || nombreExtension[1].equalsIgnoreCase("xls"))) {
            estado=false;
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
            generarArchivo();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ControlConversor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ControlConversor.class.getName()).log(Level.SEVERE, null, ex);
        }
        estado=true;
        return true;
    }

    public void convertir() {
        materiaActual = materias.get(0);
        int saltos = 0;

        numPagActual = 1;
        grupoActual = Integer.valueOf(materiaActual.getGrupo());
        planActual = materiaActual.getPlanEstudios();
        carreraActual = materiaActual.getNombrePE();
        numCarreraActual = Integer.valueOf(materiaActual.getNumeroPE());
        numMateriaActual = 0;

        int contadorMateriasHoja = 1;
        guardarLineas(crearEncabezado());
        guardarLineas(crearMateria(materiaActual));

        for (int i = 1; i < materias.size() - 1; i++) {
            numMateriaActual = i;
            materiaActual = materias.get(numMateriaActual);
            if (Integer.valueOf(materiaActual.getGrupo()) == grupoActual) {
                guardarLineas(crearMateria(materiaActual));
                contadorMateriasHoja++;
                if (contadorMateriasHoja > MaxMateriaHoja) {
                    saltos = 13;
                    guardarLineas(saltoDeHoja(saltos));
                    numPagActual++;
                    guardarLineas(crearEncabezado());
                    guardarLineas(crearMateria(materiaActual));
                    contadorMateriasHoja = 1;
                }
            } else {
                saltos = MaxCharLinea - (MaxLineaHeader + (contadorMateriasHoja * 4));
                guardarLineas(saltoDeHoja(saltos));
                numPagActual++;
                grupoActual = Integer.valueOf(materiaActual.getGrupo());
                planActual = materiaActual.getPlanEstudios();
                carreraActual = materiaActual.getNombrePE();
                numCarreraActual = Integer.valueOf(materiaActual.getNumeroPE());
                contadorMateriasHoja = 1;
                guardarLineas(crearEncabezado());
                guardarLineas(crearMateria(materiaActual));
            }
        }
    }

    public void generarArchivo() {
        String[] nombreExtension = archivo.getName().split("\\.");
        String nomGuardado = nombreExtension[0] + "_Formato.txt";
        String[] dirAux = archivo.getPath().split(archivo.getName());
        String dirNueva = dirAux[0] + nomGuardado;
        System.out.println(dirNueva);
        File fichero = new File(dirNueva);
        if (fichero.exists()) {
            System.out.println("Ya existe el archivo: " + nomGuardado);
            System.out.println("Se creara una copia");
            nomGuardado= nombreExtension[0] + "_Formato("+copiasExistentes+").txt";
            copiasExistentes++;
            dirNueva=dirAux[0] + nomGuardado;
        } 
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(dirNueva));
                for (int i = 0; i < lineas.size(); i++) {
                    bw.write(lineas.get(i));
                }
                bw.close();
            } catch (IOException ex) {
                Logger.getLogger(ControlConversor.class.getName()).log(Level.SEVERE, null, ex);
            }
            pathNew=dirNueva;
            nameNew=nomGuardado;
    }

    public String[] crearEncabezado() {
        String[] encabezado = new String[MaxLineaHeader];
        encabezado[0] = "\n";
        encabezado[1] = " " + hora() + "                                    UNIVERSIDAD AUTONOMA DE BAJA CALIFORNIA                                    " + fecha() + "\n";
        encabezado[2] = "                                               COORDINACION GENERAL DE RECURSOS HUMANOS\n";
        String paginaFTM = String.format("%3d", numPagActual);
        encabezado[3] = " RPLAN005                                           CUADRICULA DE HORARIOS                                                PAG.  " + paginaFTM + "\n";
        encabezado[4] = "                                                        Periodo : " + periodoActual() + "\n";
        encabezado[5] = "\n";
        String grupoFTM = String.format("%03d", grupoActual);
        encabezado[6] = " UNIDAD ACADEMICA: 105 FACULTAD DE INGENIERIA MXLI                                                               GRUPO: '" + grupoFTM + "' '   '" + "\n";
        String carreraFTM = String.format("%02d", numCarreraActual);
        encabezado[7] = " CARRERA: " + carreraFTM + "           " + carreraActual + "\n";
        encabezado[8] = " PLAN DE ESTUDIOS: " + planActual + "\n";
        encabezado[9] = linea() + "\n";
        encabezado[10] = " CVE.ASIGNAT.       D E S C R I P C I O N                 CAP TPO     LUNES   MARTES  MIERCO  JUEVES  VIERNES  SABADO  DOMINGO  E/S\n";
        encabezado[11] = " No.CONTROL             M A E S T R O          EDIF SALON ASG SGP\n";
        encabezado[12] = linea() + "\n";
        for (int i = 0; i < MaxLineaHeader; i++) {
            System.out.print(encabezado[i]);
        }
        return encabezado;
    }

        public String[] crearMateria(Materia materia){
         ArrayList<String> horas = new ArrayList<>();
        horas.add(materia.getLunes());
        horas.add(materia.getMartes());
        horas.add(materia.getMiercoles());
        horas.add(materia.getJueves());
        horas.add(materia.getViernes());
        horas.add(materia.getSabado());
        horas.add(materia.getDomingo());
        for(int i = 0; i<horas.size();i++){
            horas.set(i,acondicionarHoras(horas.get(i)));
        }
        String claveAsig = materia.getClaveUA();
        while(claveAsig.length()<5){
            claveAsig+=" ";
        }
        String numeroControl = materia.getNumeroControl();
        while(numeroControl.length()<4) {
            numeroControl+=" ";
        }
        String es =materia.getExtra();
        String numEmpleado = materia.getNumeroEmpleado();
        String cero ="";
        String numEmpleado2 = numEmpleado;
        while(numEmpleado.length()<6){
            cero+="0";
            numEmpleado=cero+numEmpleado2;
        }
        String nombreMateria = materia.getNombreUA().trim();
        String nomMatOrg=materia.getNombreUA().trim();
        while(nombreMateria.length()<46) nombreMateria+=" ";
        nombreMateria = nombreMateria.substring(0,45);
//        while(nombreMateria.length()<45) nombreMateria+=" ";
        String nombreProfesor=  materia.getNombreEmpleado();
        while(nombreProfesor.length()<31) nombreProfesor+=" ";
        
        String edificio = materia.getEdificio();
        String salon = materia.getSalon();
        String capacidad=materia.getCapacidad();
        String capOrg=materia.getCapacidad();
        if(capacidad.length()<2) capacidad = " "+capacidad;
        String tipo = materia.getTipo();
        String subgrupo =materia.getSubGrupo();
        if(subgrupo.equals("")) subgrupo = " ";
        if(nomMatOrg.length()>45 && capOrg.length()<2){
            nombreMateria = nomMatOrg;
            capacidad=capOrg.trim();
            nombreMateria = nombreMateria.substring(0,46);
        }
        String [] textoMateria = new String[4];
        textoMateria[0] = espacios(69)+"|"+espacios(7)+"|"+espacios(7)+"|"+espacios(7)+"|"+espacios(7)+"|"+espacios(7)+"|"+espacios(7)+"|"+espacios(7)+"|\n";
        textoMateria[1] =espacios(6)+claveAsig+espacios(3)+nombreMateria;
        /*if(textoMateria[1].length()<51) System.out.println("Si es menor a 51");
        while(textoMateria[1].length()<51){
            textoMateria[1]+=" ";
        }*/
        
        textoMateria[1]+= capacidad+espacios(2)+tipo+espacios(5)+"| "+horas.get(0).split("-")[0]+" | "+horas.get(1).split("-")[0]+" | "+horas.get(2).split("-")[0]+" | "+horas.get(3).split("-")[0]+" | "+horas.get(4).split("-")[0]+" | "+horas.get(5).split("-")[0]+" | "+horas.get(6).split("-")[0]+" | \n";

        textoMateria[2] = " "+numeroControl+espacios(4)+numEmpleado+" "+nombreProfesor.substring(0,30)+"  "+edificio+espacios(4)+salon +espacios(7)+ subgrupo+espacios(5) + "| "+horas.get(0).split("-")[1]+" | "+horas.get(1).split("-")[1]+" | "+horas.get(2).split("-")[1]+" | "+horas.get(3).split("-")[1]+" | "+horas.get(4).split("-")[1]+" | "+horas.get(5).split("-")[1]+" | "+horas.get(6).split("-")[1]+" |  "+es+"\n";
        System.out.println(textoMateria[0]);
        System.out.println(textoMateria[1]);
        System.out.println(textoMateria[2]);
        textoMateria[3] = linea()+"\n";
        System.out.println(linea());
        return textoMateria;
    }
    public String acondicionarHoras(String hora){
        String horaGeneral;
        if(!hora.equals("") && !hora.equals("     ")){
        String primeraHora =hora.split("-")[0].trim();
        String segundaHora = hora.split("-")[1].trim();
        if(primeraHora.charAt(0)=='0'){
            primeraHora=" "+primeraHora.substring(1,5);
        }
        if(segundaHora.charAt(0)=='0'){
            segundaHora=" "+segundaHora.substring(1,5);
    }
        horaGeneral = primeraHora+"-"+segundaHora;
        }else{
            horaGeneral = espacios(5)+"-"+espacios(5);
        }
        return horaGeneral;
        
    }
    public String acondicionarNombre(String nombre){
        return nombre.substring(0,30);
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
         String hora = String.format("%02d", ahora.getHour());
         String min = String.format("%02d", ahora.getMinute());
         String seg = String.format("%02d", ahora.getSecond());
        return hora + ":" + min + ":" + seg;
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

    public void guardarLineas(String[] array) {
        for (int i = 0; i < array.length; i++) {
            lineas.add(array[i]);
        }
    }

    public String[] saltoDeHoja(int cantidad) {
        String[] aux = new String[cantidad];
        for (int i = 0; i < cantidad; i++) {
            aux[i] = "\n";
            System.out.println("\n");
        }
        return aux;
    }

    public void imprimirLineas() {
        System.out.println(lineas.size());
        for (int i = 0; i < lineas.size(); i++) {
            System.out.println(lineas.get(i));
        }
    }

    public void verMaterias() {
        System.out.println("Cantidad de Materias:" + materias.size());
        for (int i = 0; i < materias.size() - 1; i++) {
            crearEncabezado();
            System.out.println(materias.get(i));
        }
    }
}
