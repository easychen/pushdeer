esptool.py --chip esp8266 --port /dev/cu.wchusbserial1410 --baud 115200 --before default_reset --after hard_reset write_flash 0x0 xxx.bin
