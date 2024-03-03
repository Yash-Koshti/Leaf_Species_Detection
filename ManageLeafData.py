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

    __CSV_field_names = ['image', 'common_name', 'scientific_name', 'class_number']

    def createSpeciesJsonFile() -> None:
        # ManageLeafData.__species.pop(len(ManageLeafData.__species) - 1)
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
    
    def readSpeciesJsonFile() -> dict:
        with open(ManageLeafData.__species_JFile, 'r') as JFile:
            data = json.load(JFile)
        return data

    def mappingLeafData(path: str, num_images: int) -> list:
        imageNameLst = ManageLeafData.getImageNameList(path)
        mappedRows = list()
        classes = ManageLeafData.readSpeciesJsonFile()
        # start_classes_at = 20
        
        # for imageName in imageNameLst:
        #     row = list()
            
        #     # Show image
        #     image = cv.imread(path + imageName)
        #     plt.imshow(cv.cvtColor(image, cv.COLOR_BGR2RGB), cmap='BrBG')
        #     plt.show()

        #     # Display classes
        #     for index in range(len(classes)):
        #         index = str(index)
        #         print(index, ")", classes[index][0], "/", classes[index][1])

        #     choice = input("\nEnter choice: ")
            
        #     row.append(imageName)
        #     row.append(classes[choice][0])
        #     row.append(classes[choice][1])
        #     row.append(int(choice))

        #     mappedRows.append(row)

        for i in range(0, len(imageNameLst), num_images):
            image_group = imageNameLst[i:i + num_images]  # Get a group of images
            image_group_count = min(len(image_group), num_images)  # Adjust count for last group

            plt.figure(figsize=[13,9])
            # Show images and take inputs for each image
            for j in range(image_group_count):
                imageName = image_group[j]

                # Show image
                image = cv.imread(path + imageName)
                plt.subplot(1, num_images, j + 1)
                plt.imshow(cv.cvtColor(image, cv.COLOR_BGR2RGB), cmap='BrBG')
                plt.axis('off')

            plt.show()

            # Display classes
            for index in range(len(classes)):
                index = str(index)
                print(index, ")", classes[index][0], "/", classes[index][1])
            # for key, names_tuple in list(classes.items())[start_classes_at : min(len(classes), (start_classes_at + num_images))]:
            #     print(str(key), ")", names_tuple[0], "/", names_tuple[1])

            # Take inputs for all images in the group
            choices = [int(choice.strip()) for choice in input("\nEnter choices for all images separated by commas: ").split(',')]

            # Validate the number of choices entered
            if len(choices) != image_group_count:
                print("Invalid number of choices entered. Please enter exactly", image_group_count, "choices.")
                continue

            # Store mapped data for each image
            for j, class_num in enumerate(choices):
                imageName = image_group[j]

                row = []
                row.append(imageName)
                row.append(classes[str(class_num)][0])
                row.append(classes[str(class_num)][1])
                row.append(class_num)

                mappedRows.append(row)
                # if class_num > start_classes_at: start_classes_at += 1

            # if len(mappedRows) == num_images * 2: break
        return mappedRows
    
    def storeInCSV(rows: list) -> None:
        try:
            csvfile = open(ManageLeafData.__leaf_data_mapped_CSVFile, 'w', newline='')
            csvfile.seek(0,2)
            csvwriter = csv.writer(csvfile)
            csvwriter.writerow(ManageLeafData.__CSV_field_names)
        except FileNotFoundError:
            csvfile = open(ManageLeafData.__leaf_data_mapped_CSVFile, 'a', newline='')
            csvfile.seek(0,2)
            csvwriter = csv.writer(csvfile)
            csvwriter.writerow(ManageLeafData.__CSV_field_names)
        except Exception as e:
            print("ERROR :", e)
        finally:
            csvwriter.writerows(rows)
            csvfile.close()

