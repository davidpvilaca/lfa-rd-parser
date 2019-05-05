echo "------------------"
echo "Compiling..."
echo "------------------\n"
make
echo "\n------------------"
echo "Compiled successfully!"
echo "To run program on terminal: \"make execute\""
echo "or java: \"cd out && java Main\""
echo "------------------\n"

while true; do
  read -p "Do you want to run the compiled program now? [Y/n]" yn
  case $yn in
    [Yy]* ) make execute; break;;
    [Nn]* ) exit;;
    * ) make execute; break;;
  esac
done
