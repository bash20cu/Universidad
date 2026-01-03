export interface Term {
    id: string;
    name: string;
    courses: Course[];
}

export interface Course {
    code: string;
    name: string;
    credits: number;
    req?: string;
    grade?: string; // "REC" or number or undefined if pending
    passed: boolean;
}

export const curriculum: Term[] = [
    {
        id: "1",
        name: "1er Cuatrimestre",
        courses: [
            { code: "SOF-01", name: "ESTRUCTURAS DISCRETAS", credits: 4, grade: "REC", passed: true },
            { code: "SOF-02", name: "INGLÉS PARA LAS TECNOLOGÍAS I", credits: 4, grade: "REC", passed: true },
            { code: "SOF-03", name: "INTRODUCCIÓN A LA COMPUTACIÓN", credits: 4, grade: "REC", passed: true },
            { code: "SOF-04", name: "TÉCNICAS DE COMUNICACIÓN", credits: 4, grade: "REC", passed: true },
        ]
    },
    {
        id: "2",
        name: "2do Cuatrimestre",
        courses: [
            { code: "SOF-05", name: "CÁLCULO I", credits: 4, req: "SOF-01", grade: "REC", passed: true },
            { code: "SOF-06", name: "INGLÉS PARA LAS TECNOLOGÍAS II", credits: 4, req: "SOF-02", grade: "REC", passed: true },
            { code: "SOF-07", name: "INVESTIGACIÓN APLICADA A LAS TECNOLOGÍAS", credits: 4, req: "SOF-04", passed: false },
            { code: "SOF-08", name: "PROGRAMACIÓN I", credits: 4, req: "SOF-03", grade: "REC", passed: true },
        ]
    },
    {
        id: "3",
        name: "3er Cuatrimestre",
        courses: [
            { code: "SOF-09", name: "CÁLCULO II", credits: 4, req: "SOF-05", grade: "REC", passed: true },
            { code: "SOF-10", name: "ESTRUCTURAS DE DATOS Y ALGORITMOS", credits: 4, req: "SOF-02, SOF-08", grade: "REC", passed: true },
            { code: "SOF-11", name: "PROBABILIDAD Y ESTADÍSTICA", credits: 4, req: "SOF-05", grade: "100.00", passed: true },
            { code: "SOF-12", name: "PROGRAMACIÓN II", credits: 4, req: "SOF-08", grade: "REC", passed: true },
        ]
    },
    {
        id: "4",
        name: "4to Cuatrimestre",
        courses: [
            { code: "SOF-13", name: "ARQUITECTURA Y ORGANIZACIÓN DE COMPUTADORES", credits: 4, req: "SOF-10, SOF-12", grade: "REC", passed: true },
            { code: "SOF-14", name: "BASE DE DATOS I", credits: 4, req: "SOF-10, SOF-12", grade: "REC", passed: true },
            { code: "SOF-16", name: "PROGRAMACIÓN III", credits: 4, req: "SOF-10, SOF-12", grade: "REC", passed: true },
            { code: "SOF-15", name: "VERIFICACIÓN Y VALIDACIÓN DE SOFTWARE", credits: 4, req: "SOF-08, SOF-10", grade: "88.00", passed: true },
        ]
    },
    {
        id: "5",
        name: "5to Cuatrimestre",
        courses: [
            { code: "SOF-19", name: "ANÁLISIS Y ESPECIFICACIÓN DE SOFTWARE", credits: 4, req: "SOF-14", grade: "89.00", passed: true },
            { code: "SOF-17", name: "BASE DE DATOS II", credits: 4, req: "SOF-14", grade: "REC", passed: true },
            { code: "SOF-18", name: "PROGRAMACIÓN IV", credits: 4, req: "SOF-16", grade: "90.00", passed: true },
            { code: "SOF-20", name: "SISTEMAS OPERATIVOS", credits: 4, req: "SOF-09, SOF-13", grade: "100.00", passed: true },
        ]
    },
    {
        id: "6",
        name: "6to Cuatrimestre",
        courses: [
            { code: "SOF-25", name: "CALIDAD DE SOFTWARE", credits: 4, req: "SOF-18, SOF-19", grade: "92.00", passed: true },
            { code: "SOF-24", name: "DISEÑO DE SOFTWARE", credits: 4, req: "SOF-19", grade: "99.00", passed: true },
            { code: "SOF-23", name: "LENGUAJES Y PARADIGMAS DE PROGRAMACIÓN", credits: 4, req: "SOF-19", grade: "98.00", passed: true },
            { code: "SOF-21", name: "REDES DE COMPUTADORES", credits: 4, req: "SOF-16, SOF-17", grade: "96.00", passed: true },
        ]
    },
    {
        id: "7",
        name: "7mo Cuatrimestre",
        courses: [
            { code: "SOF-26", name: "DISEÑO DE LA INTERACCIÓN HUMANO-COMPUTADORA", credits: 4, req: "SOF-24", grade: "100.00", passed: true },
            { code: "SOF-28", name: "INVESTIGACIÓN DE OPERACIONES", credits: 4, req: "SOF-09, SOF-11", grade: "96.00", passed: true },
            { code: "SOF-29", name: "PROCESOS DE INGENIERÍA DE SOFTWARE", credits: 4, req: "SOF-24", passed: false },
            { code: "SOF-27", name: "TÓPICOS AVANZADOS DE PROGRAMACIÓN", credits: 4, req: "SOF-23", grade: "99.00", passed: true },
        ]
    },
    {
        id: "8",
        name: "8vo Cuatrimestre",
        courses: [
            { code: "SOF-33", name: "ADMINISTRACIÓN DE PROYECTOS INFORMÁTICOS", credits: 4, req: "SOF-29", grade: "92.00", passed: true },
            { code: "SOF-30", name: "ARQUITECTURA DE SOFTWARE", credits: 4, req: "SOF-29", passed: false },
            { code: "SOF-32", name: "ELECTIVA 1", credits: 4, req: "SOF-27", passed: false },
            { code: "SOF-31", name: "INTELIGENCIA ARTIFICIAL APLICADA", credits: 4, req: "SOF-29", passed: false },
            { code: "TGINF1-N", name: "TALLER INT. GRADUACIÓN", credits: 0, passed: false },
        ]
    },
    {
        id: "9",
        name: "9no Cuatrimestre",
        courses: [
            { code: "SOF-34", name: "COMPUTACIÓN Y SOCIEDAD", credits: 4, req: "SOF-33", grade: "REC", passed: true },
            { code: "SOF-35", name: "ELECTIVA 2", credits: 4, req: "SOF-30", passed: false },
            { code: "SOF-36", name: "IMPLEMENTACIÓN Y MANTENIMIENTO DE SOFTWARE", credits: 4, req: "SOF-25, SOF-30", passed: false },
            { code: "PROYSOF01-N", name: "PROYECTO DE GRADUACIÓN", credits: 0, passed: false },
            { code: "SOF-37", name: "SEGURIDAD INFORMÁTICA", credits: 4, req: "SOF-29", passed: false },
        ]
    }
];
