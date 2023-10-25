import cv2 as cv
import matplotlib.pyplot as plt
import csv
import json
from os import listdir
from os.path import isfile, join

class ManageLeafData:
    __species = [
        ['Peepla', 'Ficus religiosa'],
        ['Borsalli', 'Mimusops elengi'],
        ['Kamani', 'Murraya paniculata'],
        ['Jasud', 'Hibiscus rosa sinensis'],
        ['Bamboo', 'Bambusa vulguris'],
        ['Clusters fig', 'Ficus racemosa'],
        ['Wedelia', 'Wedelia chinensis'],
        ['Rosy trumpet tree', 'Tabebuia rosea'],
        ['Ixora', 'Ixora coccinea'],
        ['Nagchampo', 'Plumeria pudica'], 
        ['Jatropha', 'Jatropha integerrima'],
        ['Aralia', 'Aralia variegated'],
        ['Kadam', 'Neolamarckia cadamba'],
        ['Asopalav', 'Polyalthia longifolium'],
        ['Weeping fig', 'Ficus benjamina'],
        ['Tulsi', 'Ocimum sanctum'],
        ['Jambu', 'Syzygium cumini'],
        ['Karanj', 'Pongamia pinnata'],
        ['Quick stick', 'Gliricidia sepium'],
        ['Pipal', 'Ficus amplissima']
    ]

    __species_JFile = 'Species.json'
    __leaf_data_mapped_CSVFile = 'Leaf_Data_Mapped.csv'

    __CSV_field_names = ['Image', 'Common name', 'Scientific name', 'Class number']

    def createSpeciesJsonFile() -> None:
        classes_json_data = dict(zip(range(len(ManageLeafData.__species)), ManageLeafData.__species))

        with open(ManageLeafData.__species_JFile, 'w') as Jfile:
            json.dump(classes_json_data, Jfile, indent=4)

    def addNewSpecies(name_set: list) -> None:
        if len(name_set) == 2:
            ManageLeafData.__species.append(name_set)
            ManageLeafData.createSpeciesJsonFile()
        else:
            print("Invalid Name set!\nName Set should be in ['Common name', 'Scientific name']")

    def getImageNameList(path: str) -> list:
        return [f for f in listdir(path) if isfile(join(path, f))]

    def mappingLeafData(path: str) -> list:
        imageNameLst = ManageLeafData.getImageNameList(path)
        mappedRows = list()
        
        with open(ManageLeafData.__species_JFile, 'r') as Jfile:
            _classes = json.load(Jfile)
        
        for imageName in imageNameLst:
            row = list()
            
            # Show image
            image = cv.imread(path + imageName)
            plt.imshow(image)
            plt.show()

            # Display classes
            for index in range(len(_classes)):
                index = str(index)
                print(index, ")", _classes[index][0], "/", _classes[index][1])

            choice = input("\nEnter choice: ")
            
            row.append(imageName)
            row.append(_classes[choice][0])
            row.append(_classes[choice][1])
            row.append(int(choice))

            mappedRows.append(row)
            # if len(mappedRows) == 1: break
        return mappedRows
    
    def storeInCSV(rows: list) -> None:
        try:
            csvfile = open(ManageLeafData.__leaf_data_mapped_CSVFile, 'w', newline='')
            csvwriter = csv.writer(csvfile)
        except FileNotFoundError:
            csvfile = open(ManageLeafData.__leaf_data_mapped_CSVFile, 'a', newline='')
            csvwriter = csv.writer(csvfile)
            csvwriter.writerow(ManageLeafData.__CSV_field_names)
        except Exception as e:
            print("ERROR :", e)
        finally:
            csvwriter.writerows(rows)
            csvfile.close()
