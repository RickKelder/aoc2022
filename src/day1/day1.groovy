package day1

static void main(String[] args) {
  def caloriesPerElf = [];
  int currentElfCalories = 0;
  String fileContents = new File('input.txt').getText('UTF-8').eachLine { line ->
    if (line == "") {
      caloriesPerElf << currentElfCalories;
      currentElfCalories = 0;
    } else {
      currentElfCalories += Integer.parseInt(line);
    }
  };
  caloriesPerElf.sort();
  caloriesPerElf.reverse(true);
  println("answer1: "+caloriesPerElf[0]);
  println("answer2: "+(caloriesPerElf[0]+caloriesPerElf[1]+caloriesPerElf[2]));
}