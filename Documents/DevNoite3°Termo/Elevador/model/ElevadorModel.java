package model;

public class ElevadorModel {
    public class ElevadorModel {
        private int[] posicaoElevadores;
        private int[] direcaoElevadores;
        private int elevadorSelecionado;
    
        public ElevadorModel() {
            posicaoElevadores = new int[]{0, 0};
            direcaoElevadores = new int[]{0, 0};
            elevadorSelecionado = 0;
        }
    
        public int getElevadorSelecionado() {
            return elevadorSelecionado;
        }
    
        public void setElevadorSelecionado(int elevadorSelecionado) {
            this.elevadorSelecionado = elevadorSelecionado;
        }
    
        public int[] getPosicaoElevadores() {
            return posicaoElevadores;
        }
    
        public int[] getDirecaoElevadores() {
            return direcaoElevadores;
        }
    }
    
}
