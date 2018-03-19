package algorithm;

import java.io.FileInputStream;

// 상자들이 있다. 토마토가 있는 상자는 다음날, 상하좌우에 있는 상자 속 토마토를 익힌다.
// 모든 토마토가 익는 최소 일수는?
// 0은 안 익음, 1은 익음, -1은 없음

public class Tomato_2613 {
  public static final int BOX_SIZE = 1000;
  public static int box[][];
  public static boolean check[][];

  public static int q[] = new int[3 * BOX_SIZE * BOX_SIZE];
  public static int front = -1;
  public static int rear = -1;

  public static void enQ(int x) {
    q[++rear] = x;
  }

  public static int deQ() {
    return q[++front];
  }

  public static int ripeTomato (int y, int x) {
    int days = -1;
    int delta[][] = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}}; // 상우하좌

    //익은 토마토를 찾아 큐에 넣음
    for(int i = 0; i < y; i++) {
      for(int j = 0; j < x; j++) {
        if(box[i][j] == 1) {
          enQ(i);
          enQ(j);
          enQ(1); // 익은 토마토를 1일로 설정
        }
      }
    }

    if(front == rear) {return -1;}

    while(front != rear) {
      int curY = deQ();
      int curX = deQ();
      int date = deQ();

      if(check[curY][curX] == false) {
        check[curY][curX] = true;
        box[curY][curX] = date;

        if(days < date) {days = date;} // 필요 일수 변경 여부

        for(int i = 0 ; i < delta.length; i++) { // 4방향에 대해
          int ty = curY + delta[i][0]; // 임시좌표를 만듦
          int tx = curX + delta[i][1];

          if(ty < 0 || tx < 0 || ty >= y || tx >= x) {continue;} //배열 범위초과 검사

          // 익지 않은 토마토이며 아직 체크되지 않았거나, 이미 익은 토마토인데 일자가 줄어드는 경우
          if((box[ty][tx] == 0 && check[ty][tx] == false) || box[ty][tx] > date + 1) {
            enQ(ty); // 좌표를 큐에 추가
            enQ(tx);
            enQ(date + 1); // 해당 지점의 필요 일수는 1일 증가
          }
        }

      }
    }

    // 끝난 시점에 재검사 필요
    boolean bNotRipe = false;
    int maxDays = 0;
    for(int i = 0; i < y && bNotRipe == false; i++) {
      for(int j = 0; j < x && bNotRipe == false; j++) {
        if(box[i][j] == 0) {
          bNotRipe = true;
        }
        if(box[i][j] > maxDays) {
          maxDays = box[i][j];
        }
      }
    }

    if(bNotRipe == true) { // 익지 않은 토마토가 남은 경우
      days = -1;
    } else { // 모두 익은 경우 최소 소요 일수
      days = maxDays - 1;
    }

    return days;
  }

  public static void main(String[] args) throws FileNotFoundException {
    System.setIn(new FileInputStream("2613_tomato.txt"));
    Scanner sc = new Scanner(System.in);

    int X = sc.nextInt();
    int Y = sc.nextInt();

    box = new int[Y][X];
    check = new boolean[Y][X];

    boolean bAlreadyRipe = true;
    boolean bNotYetRipe = true;

    for(int row = 0; row < Y; row++) {
      for(int col = 0; col < X; col++) {
        box[row][col] = sc.nextInt();

        if(box[row][col] == 1) { //익은 토마토
          bNotYetRipe = false;
        } else { //안 익은 토마토
          bAlreadyRipe = false;
        }
      }
    }

    int result = 0;
    if(bAlreadyRipe == true) { // 처음부터 모두 익은 경우
      result = 0;
    } else {
      result = ripeTomato(Y, X); // 토마토가 모두 익는데 필요한 최소 일수 OR []익은 토마토가 없는 경우와 안 익은 토마토가 남은 경우]에 -1
    }

    System.out.println(result + "");
    sc.close();
  }
}
