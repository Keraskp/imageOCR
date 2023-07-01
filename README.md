# eHospital LIS Image OCR API
The eHospital LIS Image OCR API is a web service that allows you to extract text from images and convert it into JSON or plain text formats. This API utilizes the Tesseract OCR engine to perform optical character recognition on the provided image file.

## Endpoints
#### Image to Json
**Endpoint:** `/image-to-json`

**Method:** POST

**Description:** Extracts text from the uploaded image file and converts it into JSON format.

Request Parameters:
| Parameter   |      Type      |           Description          |
|-------------|:--------------:|-------------------------------:|
|    image    |  report.jpg    | The image file to be processed |

Reponse:

The API responds with a JSON file containing the extracted text in key-value pairs.
```json
{
    "": "150Year(s) (Male",
    "Age: ": "50 years 10 months 16 days",
    "Lab Reference NO: ": "52",
    "Page ": "1 of",
    "SERUM CALCIUM ": "7.2 mg/dL 9-11 mgidL Verification Comment",
    "Sample Collection Date: ": "06/06:2023 05:47 PM",
    "SERUM POTASSIUM ": "45 mEqL 35-55mEQL  Verification Comment",
    "HBAL ": "52 % -650 Verification Comment",
    "Unit Name: Uit ": "1",
    "SERUM SODIUM ": "142 mEQL 135- 145 mEqL",
    "UHID: ": "2022010049138",
    "SERUM UREA ": "35 mg/dL 15-40 mg/dL",
    "SERUM CREATININE ": "204 mgldL 06- 1.5 mg/dL",
    "SERUM URIC ACID ": "6.7 mglL o mg/dL 0Year(s) - Verification Comment",
    "Government of Tripura Agartala, Tripura West ": "799006",
    "Report Upload Date: ": "06/062023 06:13 PM",
    "‘Sample Received Date: ": "06/06/2023 05:48 PM",
    "Order Date: ": "06/062023",
    "Oear - ": "50Yeat",
    "Reg Date: ": "210772022 1159 AM"
}
```

#### Image to Text
**Endpoint:** `/image-to-text`

**Method:** POST

**Description:** Extracts text from the uploaded image file in plain text format.

Request Parameters:
| Parameter   |      Type      |           Description          |
|-------------|:--------------:|-------------------------------:|
|    image    |  report.jpg    | The image file to be processed |

Reponse:

The API responds with a plain text file containing the extracted text.
```text
g;la Government Medical College & Gobind Ballay Pant Hospital
Government of Tripura Agartala, Tripura West 799006
LABORATORY OBSERVATION REPORT

UHID: 2022010049138
Patient Name: Mr. TEST TEST
Sex: Male

Order By: Dr. IPD Doctor Incharge.
Unit Name: Uit 1

Sample Collection Date: 06/06:2023 05:47 PM

Lab Name: BIOCHEMISTRY
Department: Medicine

Reg Date: 210772022 1159 AM
‘Ward Name : Male Medicine

Age: 50 years 10 months 16 days

Order Date: 06/062023

Unit In-charge: Dr. Arunsba Dasgupta
‘Sample Received Date: 06/06/2023 05:48 PM
Lab Reference NO: 52

Report Upload Date: 06/062023 06:13 PM

Bio-Cheistry Sub Centre ™
Test Name Result ification Comment
HBAL 52 % -650 Verification Comment
KIDNEY FUNCTION TEST
SERUM UREA 35 mg/dL 15-40 mg/dL
SERUM CREATININE 204 mgldL 06- 1.5 mg/dL.
Oear - 50Yeat)
SERUM URIC ACID 6.7 mglL o mg/dL 0Year(s) - Verification Comment
150Year(s) (Male)
ELECTROLYTE PROFILE
SERUM SODIUM 142 mEQL 135- 145 mEqL
SERUM POTASSIUM 45 mEqL 35-55mEQL  Verification Comment
SERUM CALCIUM 7.2 mg/dL 9-11 mgidL Verification Comment
Entered By Verified By
(Mr-HOSPITAL ADMIN) (Mr-HOSPITAL ADMIN)

#Thi

Page 1 of |

```


## Error Handling

If any error occurs during the processing of the image, the API will respond with an appropriate error message.

- If the image file is not provided or empty, a 400 Bad Request response is returned with the error message "No file provided".
- If the image file is not in a proper format or cannot be processed, a 400 Bad Request response is returned with the error message "Error! provide proper image file".
- If any other error occurs during the OCR process, a 500 Internal Server Error response is returned with the stack trace of the exception.


## Libraries and Tools Used

The following libraries and tools are used in this API:
- Tess4J: A Java wrapper for Tesseract OCR engine.
- Spring Framework: A popular Java framework for building web applications.



## Setup and Configuration

To setup up the API locally, follow these steps
1. Clone the repository.
2. Build the project using a Java build tool such as Maven or Gradle.
3. Run the application on a web server.


## Dependencies
The following dependencies are required to run the API:
- Tesseract OCR engine (with language data files)
- Java 8 or higher
- Spring Framework (version specified in project configuration)


## License
This API is open source and licensed under the MIT License.


## Contributions
Contributions to this project are welcome. If you encounter any issues or have suggestions for improvements, please open an issue or submit a pull request on the project's repository.

