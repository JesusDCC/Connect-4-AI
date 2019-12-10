import java.util.*;
public class connect4{
	static class Pair{
		int value;
		int column;
		public Pair(int valor,int coluna){
			this.value=valor;
			this.column=coluna;
		}

		public int getValue(){
			return this.value;
		}
		public void setValue(int valor){
			this.value=valor;
		}
		public int getCol(){
			return this.column;
		}
		public void setCol(int c){
			this.column = c;
		}
	}

	public static void tabela(){
		System.out.println("0|1|2|3|4|5|6");
	}
	public static void main(String[] args){
		char[][] aux = new char[6][7];
		int level=1;
		clearScreen();
		Scanner stdin = new Scanner(System.in);
		Pair bestmove = new Pair(0,0);
		Pair bestmv = new Pair(0,0);
	 	int coluna=-1;
	 	System.out.println("Escolha o algoritmo que pretende testar: ");
	 	System.out.println("1: MinMax\n2:Alpha beta\n3:Monte Carlo\n4)human");
	 	int algorithm = stdin.nextInt();
	 	while(algorithm>4 || algorithm<1){
	 		System.out.println("Opção não disponivel, escolha novamente:");
	 		algorithm = stdin.nextInt();
	 	}
	 	clearScreen();
	 	if(algorithm!=4){
	 	System.out.println("Escolha a dificuldade: ");
	 	System.out.println("1)Easy\n2)Medium\n3)Hard\n4)Impossible");
	 	level = stdin.nextInt();
	 	while(level<1 || level>4){
	 		System.out.println("Opção não disponivel, escolha novamente:");
	 		level = stdin.nextInt();
	 	}
	 }
	 
	 	clearScreen();
	 	System.out.println("1)Human vs PC\n 2)MinMax(hard) vs PC\n 3)AlphaBeta(hard) vs PC\n 4)Monte Carlo(hard) vs PC");
	 	int mode = stdin.nextInt();
	 	while(mode<1 || mode>5){
	 		System.out.println("Opção não disponivel, escolha novamente");
	 		mode = stdin.nextInt();
	 	}
		char[][] tab = new char[7][8];
		for(int i=0;i<6;i++)
			for(int j=0;j<7;j++)
				tab[i][j]='-';

		int c = -1;
		System.out.println("Tabela inicial:");
		tabela();
		printMatrix(tab);
		while((checkForWin(tab)=='-') && !endgame(tab)){
			//switch para o modo de jogo(algoritmo(modo impossivel) vs algoritmo ou humano contra algoritmo)
			switch(mode){
				case 1:
					System.out.println("Coluna em que pretende jogar:");
					c = stdin.nextInt();
					while(!isPossible(tab, c)){
						System.out.println("Jogada impossivel, escolha outra coluna:");
						c = stdin.nextInt();
					}
					break;
						
			    case 2:
					bestmove = MinMax(tab,7,false);
					c = bestmove.getCol();
					break;
				case 3:
					bestmove = AlphaBeta(tab,7,-9999,9999,false);
					c = bestmove.getCol();
					break;
				case 4: 
					c = MCTS(tab,false,3);
					break;
				default:
					System.out.println("Not possible");
				}
				clearScreen();
				aux = move(tab,c,false);
				System.out.println();
				if(checkForWin(aux)=='X'){
					tab=aux;
					System.out.println("Você jogou na coluna " + c);
					System.out.println("Tabela:");
					tabela();
					printMatrix(tab);
					break;
				}
				System.out.println("Você jogou na coluna " + c);
				System.out.println("Tabela:");
				tabela();
				printMatrix(aux);

			//switch para escolher o algoritmo contra quem o pc/humano vai jogar e a dificuldade
			switch(algorithm){
				case 1: 
					if(level==1)
						bestmove = MinMax(aux,1,true);
					
					else if(level==2)
						bestmove = MinMax(aux,3,true);
					
					else if(level==3)
						bestmove = MinMax(aux,7,true);
					else
						bestmove = MinMax(aux,8,true);
					coluna = bestmove.getCol();
					break;

				case 2:
					if(level==1)
						bestmove = AlphaBeta(aux,1,-9999,9999,true);
					
					else if(level==2)
						bestmove = AlphaBeta(aux,3,-9999,9999,true);
					
					else if(level==3)
						bestmove = AlphaBeta(aux,7,-9999,9999,true);
					else
						bestmove = AlphaBeta(aux,8,-9999,9999,true);
					coluna = bestmove.getCol();
					break;
				case 3:
					coluna = MCTS(aux,true,level);
					break;
				case 4:
				System.out.println("Coluna em que pretende jogar:");
					c = stdin.nextInt();
					while(!isPossible(tab, c)){
						System.out.println("Jogada impossivel, escolha outra coluna:");
						c = stdin.nextInt();

					}
						tab = move(aux,c,true);
						break;

				default:
					System.out.println("Impossible");
					coluna=-1;
			}
			if(algorithm!=4){
			tab = move(aux,coluna,true);
			clearScreen();
			System.out.println("Computador jogou na coluna "+ coluna);
		}
			System.out.println("Tabela:");
			tabela();
			printMatrix(tab);
		}

		if(checkForWin(tab)=='X' && algorithm==4)
			System.out.println("Perdeu para o computador");
		else if(checkForWin(tab)=='X')
			System.out.println("Parabéns, você ganhou!!");
		else if(checkForWin(tab)=='O' && algorithm==4)
			System.out.println("Parabéns,você ganhou!!");
		else if(checkForWin(tab)=='O')
			System.out.println("Perdeu para o computador :(");
		else
			System.out.println("Empate!!");
	}



	static Boolean isPossible(char[][] tab, int col){
		if(col>6 || col<0) return false;
		if(tab[0][col]=='-') return true;
		return false;
	}


	static Pair MinMax(char[][] tab ,int depth, Boolean maximizingPlayer){
		Pair bestmove = new Pair(maximizingPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE,-1);
		if(depth==0 || endgame(tab) || checkForWin(tab)!='-'){
			bestmove.setValue(evaluation(tab,maximizingPlayer));

			return bestmove;
		}
		char[][] aux = new char[6][7];
		for(int i=0;i<6;i++){
			for(int j=0;j<7;j++){
				aux[i][j]=tab[i][j];
			}
		}
		int[] moves = possibleMoves(tab);
			int k=0;
			while(k<7){
				if(moves[k]==1){
					k++;
					continue;
				}
				if(maximizingPlayer){
					Pair curr = MinMax(move(aux,k,maximizingPlayer),depth-1,!maximizingPlayer);
					if(curr.getValue()>bestmove.getValue()){
						bestmove.setValue(curr.getValue());
						bestmove.setCol(k);
					}
				}

				else {
					Pair curr = MinMax(move(aux,k,maximizingPlayer),depth-1,!maximizingPlayer);
					if(curr.getValue()<bestmove.getValue()){
						bestmove.setValue(curr.getValue());
						bestmove.setCol(k);
					}
				}
				k++;
			} 
			return bestmove;
	}
	static Pair AlphaBeta(char[][] tab ,int depth,int alpha,int beta,Boolean maximizingPlayer){
		Pair bestmove = new Pair(maximizingPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE,-1);
		if(depth==0 || endgame(tab) || checkForWin(tab)!='-'){
			bestmove.setValue(evaluation(tab,maximizingPlayer));

			return bestmove;
		}
		char[][] aux = new char[6][7];
		for(int i=0;i<6;i++){
			for(int j=0;j<7;j++){
				aux[i][j]=tab[i][j];
			}
		}
		int[] moves = possibleMoves(tab);
			int k=0;
			while(k<7){
				if(moves[k]==1){
					k++;
					continue;
				}
				if(maximizingPlayer){
					Pair curr = MinMax(move(aux,k,maximizingPlayer),depth-1,!maximizingPlayer);
					alpha = Math.max(alpha,bestmove.getValue());
					if(beta<=alpha) break;
					if(curr.getValue()>bestmove.getValue()){
						bestmove.setValue(curr.getValue());
						bestmove.setCol(k);
					}
				}

				else {
					Pair curr = MinMax(move(aux,k,maximizingPlayer),depth-1,!maximizingPlayer);
					beta = Math.min(beta,bestmove.getValue());
					if(beta<=alpha) break;
					if(curr.getValue()<bestmove.getValue()){
						bestmove.setValue(curr.getValue());
						bestmove.setCol(k);
					}
				}
				k++;
			} 
		return bestmove;
	}

	public static char[][] move(char[][] pai ,int col, Boolean maximizingPlayer){
		int i=5;
		char[][] board = new char[6][7];
		for(int k=0;k<6;k++)
			for(int j=0;j<7;j++)
				board[k][j]=pai[k][j];

		while(board[i][col]!='-' && i>=0){i--;}

		if(maximizingPlayer)
			board[i][col]='O';
		else 
			board[i][col]='X';

		return board;
	}

	public static int evaluation(char[][] tab, Boolean maximizingPlayer){
	int sum = 0;
	int countX = 0;
	int countO = 0;
	int turn = 0;

	if(endgame(tab) && checkForWin(tab)=='-') return 0;

	if (maximizingPlayer)
		turn = 16;
	else
		turn = -16;

	//HORIZONTAL CHECK
	for(int row=0;row<6;row++){
		for(int col=0;col<4;col++){
			countX=0;
			countO=0;

			if(tab[row][col]=='O')
				countO++;
			else if(tab[row][col]=='X')
				countX++;
			if(tab[row][col+1]=='O')
				countO++;
			else if(tab[row][col+1]=='X')
				countX++;
			if(tab[row][col+2]=='O')
				countO++;
			else if(tab[row][col+2]=='X')
				countX++;
			if(tab[row][col+3]=='O')
				countO++;
			else if(tab[row][col+3]=='X')
				countX++;

			if(countX==4)
				return -512+turn;
			if(countO==4)
				return 512+turn;
			if(countX==3 && countO==0)
				sum+= -50;
			if(countO==3 && countX==0)
				sum+=50;
			if(countX==2 && countO==0)
				sum+= -10;
			if(countO==2 && countX==0)
				sum+=10;
			if(countX==1 && countO==0)
				sum+= -1;
			if(countO==1 && countX==0)
				sum+= 1;
		}
	}


	//VERTICAL CHECK
	for(int row=0;row<7;row++){
		for(int col=5;col>2;col--){
			countO=0;
			countX=0;

			if(tab[col][row]=='O')
				countO++;
			else if(tab[col][row]=='X')
				countX++;
			if(tab[col-1][row]=='O')
				countO++;
			else if(tab[col-1][row]=='X')
				countX++;
			if(tab[col-2][row]=='O')
				countO++;
			else if(tab[col-2][row]=='X')
				countX++;
			if(tab[col-3][row]=='O')
				countO++;
			else if(tab[col-3][row]=='X')
				countX++;

			if(countX==4)
				return -512+turn;
			else if(countO==4)
				return 512+turn;
			else if(countX==3 && countO==0)
				sum+= -50;
			else if(countO==3 && countX==0)
				sum+=50;
			else if(countX==2 && countO==0)
				sum+= -10;
			else if(countO==2 && countX==0)
				sum+=10;
			else if(countX==1 && countO==0)
				sum+= -1;
			else if(countO==1 && countX==0)
				sum+= 1;


		}
	}

	//diagonal down
		for(int j=3; j>=0; j--){
			for(int i=0; i<3; i++){
				countO=0;
				countX=0;
				if(tab[i][j]=='O')
					countO++;
				else if(tab[i][j]=='X')
					countX++;
				if(tab[i+1][j+1]=='O')
					countO++;
				else if(tab[i+1][j+1]=='X')
					countX++;
				if(tab[i+2][j+2]=='O')
					countO++;
				else if(tab[i+2][j+2]=='X')
					countX++;
				if(tab[i+3][j+3]=='O')
					countO++;
				else if(tab[i+3][j+3]=='X')
					countX++;


				if(countO==4)
					return 512+turn;
				else if(countX==4)
					return -512+turn;
				else if(countO==3 && countX==0)
					sum+= 50;	
				else if(countX==3 && countO==0)
					sum+=-50;
				else if(countO==2 && countX==0)
					sum+= 10;
				else if(countX==2 && countO==0)
					sum+=-10;
				else if(countO==1 && countX==0)
					sum+=1;			
				else if(countX==1 && countO==0)
					sum+=-1;
			}
		}

		//diagonal up
		for(int j=0; j<4; j++){
			for(int i=3; i<6; i++){
				countO=0;
				countX=0;
				if(tab[i][j]=='O')
					countO++;
				else if(tab[i][j]=='X')
					countX++;
				if(tab[i-1][j+1]=='O')
					countO++;
				else if(tab[i-1][j+1]=='X')
					countX++;
				if(tab[i-2][j+2]=='O')
					countO++;
				else if(tab[i-2][j+2]=='X')
					countX++;
				if(tab[i-3][j+3]=='O')
					countO++;
				else if(tab[i-3][j+3]=='X')
					countX++;

				if(countO==4)
					return 512+turn;
				else if(countX==4)
					return -512+turn;
				else if(countO==3 && countX==0)
					sum+= 50;
				else if(countX==3 && countO==0)
					sum+= -50;
				else if(countO==2 && countX==0)
					sum+= 10;
				else if(countX==2 && countO==0)
					sum+= -10;
				else if(countO==1 && countX==0)
					sum+=1;
				else if(countX==1 && countO==0)
					sum+= -1;
				
			}
		}

return sum+turn;



}

	public static Boolean endgame(char[][] tab){
		for(int i=0;i<7;i++){
				if(tab[0][i]=='-'){
					return false;
				}
			}
			return true;
	}

	public static int[] possibleMoves(char[][] tab){
		int[] moves = new int[7];
		Arrays.fill(moves,1);
		for(int i=0;i<7;i++){
				if(tab[0][i]=='-'){
					moves[i]=0;
				}
			}
		return moves;
		}

	public static void printMatrix(char[][] tab){
	for (int i = 0; i < 6; i++) {
    	for (int j = 0; j < 7; j++) {
        	System.out.print(tab[i][j] + " ");
    	}
    	System.out.println();
	}
}

	public static char checkForWin(char[][] board){
		//horizontal
		for(int row = 0; row < 6; row++){
			for(int col = 0; col < 4; col++){
				if((board[row][col]!='-') && (board[row][col] == board[row][col+1]) && (board[row][col+1] == board[row][col+2]) && (board[row][col+2] == board[row][col+3]))
					return board[row][col];
			}
		}			
		//vertical
		for(int row = 0; row < 3; row++) {
			for(int col = 0; col < 7; col++){
				if((board[row][col]!='-') && (board[row][col]==board[row+1][col])&&(board[row][col]==board[row+2][col])&&(board[row][col] == board[row+3][col]))
					return board[row][col];
			}
		}
		//diagonal down
		for(int col=3; col>=0; col--){
			for(int row=0; row<3; row++){
				if((board[row][col]!='-') && (board[row][col]==board[row+1][col+1])&&(board[row][col]==board[row+2][col+2])&&(board[row][col]==board[row+3][col+3]))
					return board[row][col];
			}
		}
		//diagonal up
		for(int col=0; col<4; col++){			
			for(int row=3; row<6; row++){
				if((board[row][col]!='-') && (board[row][col]==board[row-1][col+1])&&(board[row][col]==board[row-2][col+2])&&(board[row][col]==board[row-3][col+3]))
					return board[row][col];
			}
		}

		return '-'; //sem vencedor
	}
		static int MCTS(char[][] tab,Boolean maximizingPlayer, int level){
		rootNode = new TreeNode(tab,null);
		int n = 0;
		if(level==1) n = 5000;
		else if(level==2) n = 300000;
		else if(level==3) n = 500000;
		else n = 1000000;
		for(int i=0;i<n;i++){
			TreeNode cur = select(rootNode,maximizingPlayer);
			updateStats(simulate(cur,maximizingPlayer),cur);
		}
		return getRecommendedMove(rootNode);
	}

	static class TreeNode{
		float pWins = 0;
		int timesVisited = 0;
		int numChild = 0;
		char[][] matrix;
		TreeNode parent;
		TreeNode[] child;
	
		public TreeNode(char[][] m, TreeNode p){
			this.matrix = m;
			this.parent = p;
			this.child = new TreeNode[7];
		}
		public char[][] getM(){
			return this.matrix;
		}
	}

	static Double simulate(TreeNode cur,Boolean maximizingPlayer){
		Random r = new Random();	
		char[][] aux = new char[6][7];
		for(int i=0;i<6;i++){
			for(int j=0;j<7;j++){
				aux[i][j]=cur.getM()[i][j];
			}
		}		
		if(checkForWin(aux)=='-' && !endgame(aux)){
			int moveIndex;
			while(checkForWin(aux)=='-' && !endgame(aux)){
				int low = 0;
				int high = 7;
				int result = r.nextInt(high-low) + low;
				while(!isPossible(aux,result)){
					result = r.nextInt(high-low) + low;
				}
				moveIndex = result;
				aux = move(aux,moveIndex,maximizingPlayer);
				maximizingPlayer=!maximizingPlayer;
			}
		}
		if(checkForWin(aux)=='O')
			return 1.0;
		else if(checkForWin(aux)=='-')
			return 0.5;
		else
			return 0.0;
	}

	static void incrementNumChild(TreeNode pai){
		while(pai!=null){
			pai.numChild++;
			pai=pai.parent;
		}
	}
	static TreeNode select(TreeNode pai,Boolean maximizingPlayer){
		//if parent has at least a child without statistics,select parent
		TreeNode cur = pai;	
		char[][] aux = new char[6][7];
	
		while(availableMoves(cur.getM())>0){
			for(int i=0;i<7;i++){
				if(isPossible(cur.getM(),i)){
					if(cur.child[i]==null){
						for(int k=0;k<6;k++){
							for(int j=0;j<7;j++){
								aux[k][j]=cur.getM()[k][j];
							}
						}
						cur.child[i]=new TreeNode((move(aux,i,maximizingPlayer)),cur);//argumentos do no
						incrementNumChild(cur);
						return cur.child[i];
					}
				}
			}
			//chegando aqui todas os childs de cur existem, queremos descer na arvore, bestchildren decido a rota
			cur = cur.child[bestchildindex(cur,maximizingPlayer)];
			maximizingPlayer=!maximizingPlayer;
		}
		return cur; // terminal state, vai ser "testado"	
	}

	static int availableMoves(char[][] tab){
		int[] moves = possibleMoves(tab);
		int available=0;
		for(int i=0;i<7;i++){
			if(moves[i]==0){
				available++;
			}
		}
		return available;
	}



    static int getRecommendedMove(TreeNode root){
        if(root!=null && root.numChild> 0) {
            return moreVisited(root);}
   		else
   			return -1;
    }

	static TreeNode rootNode;
	static int moreVisited(TreeNode root){
		double max=0;
		int maxIndex=-1;
		for(int i=0;i<7;i++){
			if(root.child[i]!=null){
				if(root.child[i].timesVisited>max){
					max = root.child[i].timesVisited;
					maxIndex = i;
				}

			}
		}
		return maxIndex;
	}

	static int bestchildindex(TreeNode pai,Boolean maximizingPlayer){
		int maxIndex = -1;
		double maxValue=-9999;
		double curValue=0;
		double winRate=0;
		TreeNode cur = pai;
		for(int i=0;i<7;i++){

			if(cur.child[i]==null)
				continue;
			TreeNode curchild = cur.child[i];

			double wins = maximizingPlayer ? curchild.pWins : curchild.timesVisited - curchild.pWins;

			winRate = wins/curchild.timesVisited;

			curValue = winRate + Math.sqrt(2) * Math.sqrt(2 * Math.log(cur.timesVisited) / curchild.timesVisited);
			if(curValue>maxValue || (curValue == maxValue && Math.random()<0.5) ){
				maxValue = curValue;
				maxIndex = i;
			}
		}
		return maxIndex;
	}

	static void updateStats(Double n, TreeNode cur){
        while (cur!=null) {
            cur.pWins+=n;
            cur.timesVisited += 1;
            cur = cur.parent;
        }
    }


    public static void clearScreen(){  
    	System.out.print("\033[H\033[2J");  
    	System.out.flush();  
   	}
}
