i=2;while [ $i -le 32 ]; do sed -e s/1/$i/ process/p1.java > process/p$i.java;i=$(($i+1));done
