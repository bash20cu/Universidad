var canvas = document.getElementById("mapa");
let Arista = "Arista-";

if (canvas && canvas.getContext) {
  var ctx = canvas.getContext("2d");
}

$(document).ready(function () {
  clearInformation();
  sizeCanvas();
  drawGrid();

  $("#btnClear").click(function () {
    clearInformation();
    sizeCanvas();
    drawGrid();
  });

  $("#btnSave").click(function () {
    var jsonData = JSON.stringify(grafo);
    guardar(jsonData, "grafo.json", "json/plain");
  });

  //Crear Vertice en el plano , usando el mouse click derecho
  canvas.addEventListener("click", function (event) {
    let x = event.offsetX;
    let y = event.offsetY;
    nameV = prompt("Nombre del vertice?:").toUpperCase();

    if (nameV != "" && nameV != null) {
      //let coordenada = [x, y];
      let existe = false;
      let queExiste = "";

      // Valida que el nombre del vertice a ingresar no tenga el mismo nombre que alguno ya almacenado.
      if ([nameV] in grafo) {
        existe = true;
        queExiste += `Ya existe un vertice con el nombre ${nameV} | `;
      }

      if (!existe) {
        coordenadas[nameV] = [x, y];
        grafo[nameV] = { x: x, y: y };
        $(".form select option").each(function () {
          $(this).remove();
        });
        drawVertex(x, y, 7, nameV);
        fillSelects();
      } else {
        alert(queExiste);
      }
    } else {
      coordenadas[nameV] = [x, y];
      $(".form select option").each(function () {
        $(this).remove();
      });
      drawVertex(x, y, 7, nameV);
      //fillSelects();
      clearFormData();
      $("#positionX").focus();
    }
  });

  $("#btnCrearE").click(function (e) {
    e.preventDefault();
    let initialV = $("#initialV").val();
    let finalV = $("#finalV").val();
    let peso = $("#peso").val();

    if (peso <= 0 && peso === "") {
      peso = 1;
    }

    if (initialV != "" && finalV != "" && initialV != null && finalV != null) {
      if (initialV != finalV) {
        let x1 = coordenadas[initialV][0];
        let y1 = coordenadas[initialV][1];
        let x2 = coordenadas[finalV][0];
        let y2 = coordenadas[finalV][1];
        Arista = Arista + initialV + finalV;

        if (aristas.length > 0) {
          let existe = false;
          let queExiste = `Ya existe una arista desde el vértice ${initialV} hasta el vértice ${finalV}.`;

          for (let i = 0; i < aristas.length; i++) {
            if (
              (aristas[i][0] == initialV && aristas[i][1] == finalV) ||
              (aristas[i][0] == finalV && aristas[i][1] == initialV)
            ) {
              existe = true;
              i = aristas.length;
            }
          }

          if (!existe) {
            grafo[initialV][finalV] = peso;
            grafo[finalV][initialV] = peso;
            aristas.push([initialV, finalV]);
            aristas.push([finalV, initialV]);
            drawEdge(x1, y1, x2, y2, peso);
            clearFormData();
            grafo[Arista] = {
              initialV: initialV,
              finalV: finalV,
              x: coordenadas[initialV][0],
              y: coordenadas[initialV][1],
              x2: coordenadas[finalV][0],
              y2: coordenadas[finalV][1],
              peso: peso,
            };
            Arista = "Arista-";
          } else {
            alert(queExiste);
          }
        } else {
          grafo[initialV][finalV] = peso;
          grafo[finalV][initialV] = peso;
          aristas.push([initialV, finalV]);
          aristas.push([finalV, initialV]);
          drawEdge(x1, y1, x2, y2, peso);
          clearFormData();

          grafo[Arista] = {
            initialV: initialV,
            finalV: finalV,
            x: coordenadas[initialV][0],
            y: coordenadas[initialV][1],
            x2: coordenadas[finalV][0],
            y2: coordenadas[finalV][1],
            peso: peso,
          };
          Arista = "Arista-";
        }
      } else {
        alert(
          `No se puede conectar ${initialV} con ${finalV}, ya que son los mismos vértices. El sistema no lo acepta.`
        );
      }
    } else {
      alert("Por favor introduce los datos.");
    }
  });

  $("#btnCalcRoute").click(function (e) {
    e.preventDefault();
    let initialV = $("#initialVC").val();
    let finalV = $("#finalVC").val();

    if (initialV != "" && finalV != "" && initialV != null && finalV != null) {
      if (initialV != finalV) {
        let dks = dijkstra(grafo, initialV, finalV);
        let distance = dks["distancia"];
        let route = dks["ruta"];

        for (let i = 0; i < route.length; i++) {
          if (i < route.length - 1) {
            drawEdge(
              coordenadas[route[i]][0],
              coordenadas[route[i]][1],
              coordenadas[route[i + 1]][0],
              coordenadas[route[i + 1]][1],
              null,
              900,
              true
            );
            $("#distance").html(
              `<strong>Peso total:</strong> ${distance}<br><strong>Ruta:</strong> (${route})`
            );
            $("#distance").slideDown(300);
          }
        }
      } else {
        alert("¡Ya estás en tu destino!");
      }
    } else {
      alert("Por favor introduce los datos.");
    }
  });
});

function sizeCanvas(size = 900) {
  canvas.width = size;
  canvas.height = size;
}

function drawGrid(size = 900) {
  ctx.strokeStyle = "#F5F5F5";

  for (var x = 0; x <= size; x += 6) {
    ctx.moveTo(x, 0);
    ctx.lineTo(x, size);
  }

  for (var y = 0; y <= size; y += 6) {
    ctx.moveTo(0, y);
    ctx.lineTo(size, y);
  }

  ctx.stroke();
}

function emptyCanvas(size = 900) {
  canvas.width = size;
  drawGrid();
}

function drawVertex(x, y, r = 7, nameOfVertex, size = 900) {
  let escala = Math.round(size / 100);
  let xPixel = x;
  let yPixel = y;

  if (ctx) {
    ctx.textAlign = "center";
    ctx.font = "10pt Verdana";
    ctx.fillStyle = "#000000";
    ctx.fillText(nameOfVertex, x, y + 23);

    ctx.fillStyle = "#7030A0";
    ctx.beginPath();
    ctx.arc(x, y, r, 0, 2 * Math.PI);
    ctx.fill();
  }
}

function drawEdge(x1, y1, x2, y2, weight = null, optimalRoute = false) {
  let xMedioPixel = Math.round((x1 + x2) / 2);
  let yMedioPixel = Math.round((y1 + y2) / 2);

  if (ctx) {
    if (optimalRoute) {
      ctx.beginPath();
      ctx.strokeStyle = "#C00303";
    } else {
      ctx.textAlign = "center";
      ctx.font = "10pt Verdana";
      ctx.fillStyle = "#000000";
      ctx.fillText(weight, xMedioPixel, yMedioPixel + 23);
      ctx.strokeStyle = "#7030A0";
    }

    ctx.lineWidth = 3;
    ctx.moveTo(x1, y1);
    ctx.lineTo(x2, y2);
    ctx.stroke();
  }
}

function clearInformation() {
  emptyCanvas();
  $(".form input:not(input[type='submit'])").empty();
  $(".form select").empty();
  $("#distance").html("");
  $("#distance").slideUp(300);
  for (const o in grafo) {
    delete grafo[o];
  }
}

function clearFormData() {
  $(".form input:not(input[type='submit'])").val("");
  $(".form select").val("");
}

function clearForms() {
  $(".form").slideUp(300);
}

function fillSelects() {
  let initialV = document.getElementById("initialV");
  let finalV = document.getElementById("finalV");
  let initialVC = document.getElementById("initialVC");
  let finalVC = document.getElementById("finalVC");

  let optionDefault1 = document.createElement("option");
  optionDefault1.text = "...";
  optionDefault1.value = "";
  initialV.add(optionDefault1);

  let optionDefault2 = document.createElement("option");
  optionDefault2.text = "...";
  optionDefault2.value = "";
  finalV.add(optionDefault2);

  let optionDefault3 = document.createElement("option");
  optionDefault3.text = "...";
  optionDefault3.value = "";
  initialVC.add(optionDefault3);

  let optionDefault4 = document.createElement("option");
  optionDefault4.text = "...";
  optionDefault4.value = "";
  finalVC.add(optionDefault4);

  let nodos = Object.keys(grafo);
  for (let i = 0; i < nodos.length; i++) {
    if(!nodos[i].match("Arista")){
      let optionInitial = document.createElement("option");
      optionInitial.text = nodos[i];
      optionInitial.value = nodos[i];
      initialV.add(optionInitial);
  
      let optionInitialC = document.createElement("option");
      optionInitialC.text = nodos[i];
      optionInitialC.value = nodos[i];
      initialVC.add(optionInitialC);
  
      let optionFinal = document.createElement("option");
      optionFinal.text = nodos[i];
      optionFinal.value = nodos[i];
      finalV.add(optionFinal);
  
      let optionFinalC = document.createElement("option");
      optionFinalC.text = nodos[i];
      optionFinalC.value = nodos[i];
      finalVC.add(optionFinalC);
    }
  }
}

function guardar(content, fileName, contentType) {
  var a = document.createElement("a");
  var file = new Blob([content], { type: contentType });
  a.href = URL.createObjectURL(file);
  a.download = fileName;
  a.click();
}

//cargar el archivo con los datos y dibujar en el plano
function cargar(event) {
  clearInformation();
  $(".form select option").each(function () {
    $(this).remove();
  });

  var input = event.target;
  var reader = new FileReader();
  reader.onload = function () {
    var text = reader.result;
    grafo = JSON.parse(reader.result);

    for (let nodo in grafo) {
      if (!nodo.includes("Arista")) {
        drawVertex(grafo[nodo].x, grafo[nodo].y, 7, nodo);
        coordenadas[nodo] = [grafo[nodo].x, grafo[nodo].y];

        let nodos = nodo.toString();
        optionInitial = document.createElement("option");
        optionInitial.text = nodos;
        optionInitial.value = nodos;
        initialV.add(optionInitial);

        optionInitialC = document.createElement("option");
        optionInitialC.text = nodos;
        optionInitialC.value = nodos;
        initialVC.add(optionInitialC);

        optionFinal = document.createElement("option");
        optionFinal.text = nodos;
        optionFinal.value = nodos;
        finalV.add(optionFinal);

        optionFinalC = document.createElement("option");
        optionFinalC.text = nodos;
        optionFinalC.value = nodos;
        finalVC.add(optionFinalC);
      } else {
        drawEdge(
          grafo[nodo].x,
          grafo[nodo].y,
          grafo[nodo].x2,
          grafo[nodo].y2,
          grafo[nodo].peso
        );
      }
    }
  };

  reader.readAsText(input.files[0]);
}

function freeObject(object) {
  const keys = Object.keys(object);
  for (const key of keys) {
    delete object[key];
  }
}
