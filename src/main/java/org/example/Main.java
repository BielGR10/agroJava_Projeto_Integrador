package org.example;
import java.util.Scanner;

public class Main {
    static void main() {
        Scanner input = new Scanner(System.in);

        double[] volume = new double[7];
        int linha = 0;
        int coluna = 0;
        double[][] umidade = new double[4][4];
        int escolha = 0, dia = 0;
        boolean dados = false;

        do{
            System.out.println("""
                    1) Cadastrar Dados
                    2) Exibir Mapa do Campo
                    3) Relatório de Alertas de Irrigação
                    4) Sair
                    """);
            escolha = input.nextInt();

            switch(escolha){
                case 1:
                    for(int i=0; i< volume.length; i++) {
                        System.out.println("Digite o volume da chuva (em mm) do dia " + (dia + 1));
                        volume[dia] = input.nextDouble();
                        dia++;
                    }
                case 2:
                    if(dados == false){
                        for (int i=0 ; i <=15; i++) {


                            System.out.println("Qual a umidade do talhão "+(linha+1)+"X"+(coluna+1));
                            System.out.println("""
                                       1 2 3 4
                                    1  X X X X
                                    2  X X X X
                                    3  X X X X
                                    4  X X X X
                                    """+linha);
                            umidade[linha][coluna] =  input.nextDouble();
                            linha++;
                            if(linha == 4){
                                linha = 0;
                                coluna++;
                            }
                            if(i == 15){
                                dados = true;
                            }
                        }
                    }else{
                        System.out.println("   1    2    3    4");
                        System.out.println("1 "+umidade[0][0]+" "+umidade[1][1]+" "+umidade[2][2]+" "+umidade[3][3]);
                        System.out.println("2 "+umidade[1][0]+" "+umidade[1][1]+" "+umidade[1][2]+" "+umidade[1][3]);
                        System.out.println("3 "+umidade[2][0]+" "+umidade[2][1]+" "+umidade[2][2]+" "+umidade[2][3]);
                        System.out.println("4 "+umidade[3][0]+" "+umidade[3][1]+" "+umidade[3][2]+" "+umidade[3][3]);
                    }
            }
        }while(escolha != 4);

    }
}
