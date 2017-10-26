package com.example.janderson.inventario.Model;


import android.os.Environment;
import android.widget.EditText;
import java.io.File;
import java.io.IOException;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class Excel {

    private File file;
    private File folder = new File(Environment.getExternalStorageDirectory()+"/Inventario");
    private WritableWorkbook wb = null;

    public boolean verificaArquivo(){

        if (!folder.exists()) {
            folder.mkdir();
        }

        file = new File(folder, "Inventario.xls");
        if (!file.exists()){
            try {
                criaArquivo();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (BiffException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    private void criaArquivo() throws IOException, BiffException, WriteException {
        //cria workbook

        try {
            wb = Workbook.createWorkbook(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Cria planilha
        wb.createSheet("Inventário", 0);

        WritableSheet plan = wb.getSheet(0);

        //Escreve na célula
        Label lblTombo = new Label(0,0,"Tombo");
        Label lblLocal = new Label(1,0,"Local");
        Label lblDescricao = new Label(2,0,"Descrição");

        // Como o método pode levantar exceção
        // iremos coloca-lo dentro de um try/catch
        try {

            plan.addCell(lblTombo); //add celula
            plan.addCell(lblLocal);
            plan.addCell(lblDescricao);

            wb.write(); //grava
            wb.close();

        } catch (RowsExceededException e1) {
            e1.printStackTrace();
        } catch (WriteException e1) {
            e1.printStackTrace();
        }
    }

    public void gravaLinha(String tombo, String local, String descricao) throws IOException, WriteException, BiffException {
        Workbook workbook = null;
        File file2 = new File(folder, "Inventario.xls");

        try{
            File folder2 = new File(Environment.getExternalStorageDirectory()+"/Inventario/Inventario.xls");
            workbook = Workbook.getWorkbook(folder2);
            wb = Workbook.createWorkbook(file2, workbook);
            Sheet sheet = workbook.getSheet(0);

            WritableSheet plan1 = wb.getSheet(0);

            Label lblTombo = new Label(0, sheet.getRows(), tombo);
            Label lblLocal = new Label(1, sheet.getRows(), local);
            Label lblDescricao = new Label(2, sheet.getRows(), descricao);

            plan1.addCell(lblTombo);
            plan1.addCell(lblLocal);
            plan1.addCell(lblDescricao);

            wb.write();
            wb.close();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
