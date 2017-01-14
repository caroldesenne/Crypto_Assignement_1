/*
 * Carolina de Senne Garcia
 * INF 568 - Advanced Cryptology
 * Assignement 1 - SHAKE256 function
 */

public class SHAKE256 {
	public static final int b = 1600;
	public static final int w = 64;
	public static final int c = 512;

	// THETA FUNCTION
	public static char[][][] theta(char[][][] A) {
		char[][] C = new char[5][w];
		char[][] D = new char[5][w];
		char[][][] A_line = new char[5][5][w];
		// calculate C
		for(int x = 0; x < 5; x++) {
			for(int z = 0; z < w; z++) {
				C[x][z] = (char) (A[x][0][z]^A[x][1][z]^A[x][2][z]^A[x][3][z]^A[x][4][z]);
			}
		}
		// calculate D
		for(int x = 0; x < 5; x++) {
			for(int z = 0; z < w; z++) {
				int i = x-1 % 5;
				if(i < 0)
					i += 5;
				int k = z-1 % w;
				if(k < 0)
					k += w;
				D[x][z] = (char) (C[i][z]^C[(x+1)%5][k]);
			}
		}
		// calculate A'
		for(int x = 0; x < 5; x++) {
			for(int y = 0; y < 5; y++) {
				for(int z = 0; z < w; z++) {
					A_line[x][y][z] = (char) (A[x][y][z]^D[x][z]);
				}
			}
		}
		return A_line;
	}

	// RHO FUNCTION
	public static char[][][] rho(char[][][] A) {
		char[][][] A_line = new char[5][5][w];
		for(int z = 0; z < w; z++)
			A_line[0][0][z] = A[0][0][z];
		int x = 1;
		int y = 0;
		for(int t = 0; t < 23; t++) {
			for(int z = 0; z < w; z++) {
				int k = z - (t+1)*(t+2)/2 % w;
				if(k < 0)
					k += w;
				A_line[x][y][z] = A[x][y][k];
			}
			x = y;
			y = (2*x)+(3*y) % 5;
		}
		return A_line;
	}

	// PI FUNCTION
	public static char[][][] pi(char[][][] A) {
		char[][][] A_line = new char[5][5][w];
		for(int x = 0; x < 5; x++) {
			for(int y = 0; y < 5; y++) {
				for(int z = 0; z < w; z++) {
					A_line[x][y][z] = A[(3*y+x)%5][x][z];
				}
			}
		}
		return A_line;
	}

	// CHI FUNCTION
	public static char[][][] chi(char[][][] A) {
		char[][][] A_line = new char[5][5][w];
		for(int x = 0; x < 5; x++) {
			for(int y = 0; y < 5; y++) {
				for(int z = 0; z < w; z++) {
					A_line[x][y][z] = (char) (A[x][y][z]^(A[x+1%5][y][z]^1) & A[x+2%5][y][z]);
				}
			}
		}
		return A_line;
	}

	// RC AUXILIAR FUNCTION TO IOTA
	public static char rc(int t) {
		if(t % 255 == 0)
			return 1;
		int r = 128;				//10000000
		int anulate_first = ~128;	//01111111
		for(int i = 0; i < (t % 255); i++) {
			int zero = 0;
			int bit8 = r & 1;
			r = (r >> 1) & anulate_first;
			int mask = (bit8 << 1) + (bit8 << 2) + (bit8 << 3) + (bit8 << 7);	//b000bbb0
			r = r^mask;
		}
		char rc = (char) (r >> 7) & 1;
		return rc;
	}
	// IOTA FUNCTION
	public static char[][][] iota(char[][][] A, int i_r) {
		char[][][] A_line = new char[5][5][w];
		for(int x = 0; x < 5; x++) {
			for(int y = 0; y < 5; y++) {
				for(int z = 0; z < w; z++) {
					A_line[x][y][z] = A[x][y][z];
				}
			}
		}
		char[] RC = char[w];
		for(int i = 0; i < w; i++)
			RC[i] = 0;
		int l = (int) (Math.log(w)/Math.log(2));
		for(int j = 0; j < l; j++) {
			RC[(1<<j)-1] = rc(j+7*i_r);
		}
		for(int z = 0; z < w; z++) {
			A_line[0][0][z] = (char) (A_line[0][0][z]^RC[z]);
		}
		return A_line;
	}
 
	public static void main(String[] args) {
		char[][][] A = new char[5][5][w];

	}
}