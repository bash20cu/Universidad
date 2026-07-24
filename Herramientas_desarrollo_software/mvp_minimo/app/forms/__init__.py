"""Formularios WTForms con validaciones de entrada del sistema."""

from flask_wtf import FlaskForm
from wtforms import IntegerField, PasswordField, SelectField, StringField, SubmitField, TextAreaField
from wtforms.validators import DataRequired, Email, EqualTo, Length, NumberRange, Optional, Regexp, URL


class LoginForm(FlaskForm):
    """Recoge las credenciales iniciales antes del segundo factor."""

    username = StringField("Usuario", validators=[DataRequired(), Length(max=80)])
    password = PasswordField("Contraseña", validators=[DataRequired(), Length(max=128)])
    submit = SubmitField("Continuar")


class TwoFactorForm(FlaskForm):
    """Valida que el código recibido tenga exactamente seis dígitos."""

    code = StringField(
        "Código de verificación",
        validators=[DataRequired(), Regexp(r"^\d{6}$", message="Ingresa 6 dígitos.")],
    )
    submit = SubmitField("Verificar")


class TotpForm(FlaskForm):
    """Valida códigos TOTP de seis dígitos generados por el autenticador."""

    code = StringField(
        "Código de autenticación",
        validators=[DataRequired(), Regexp(r"^\d{6}$", message="Ingresa 6 dígitos.")],
    )
    submit = SubmitField("Verificar código")


class EmailForm(FlaskForm):
    """Formulario reutilizable para capturar un correo válido."""

    email = StringField("Correo", validators=[DataRequired(), Email(), Length(max=255)])


class RegistrationForm(FlaskForm):
    """Valida el autorregistro de una cuenta estudiantil."""

    username = StringField("Usuario", validators=[DataRequired(), Length(min=3, max=80)])
    email = StringField("Correo electrónico", validators=[DataRequired(), Email(), Length(max=255)])
    password = PasswordField("Contraseña", validators=[DataRequired(), Length(min=8, max=128)])
    confirm_password = PasswordField(
        "Confirmar contraseña",
        validators=[DataRequired(), EqualTo("password", message="Las contraseñas deben coincidir.")],
    )
    submit = SubmitField("Crear mi cuenta")


class StudentForm(FlaskForm):
    """Valida los datos del perfil académico de un estudiante."""

    name = StringField("Nombre completo", validators=[DataRequired(), Length(max=150)])
    age = IntegerField("Edad", validators=[DataRequired(), NumberRange(min=5, max=120)])
    school = StringField("Centro educativo", validators=[DataRequired(), Length(max=180)])
    interest_area = StringField("Área de interés", validators=[DataRequired(), Length(max=120)])
    assigned_level = SelectField(
        "Nivel asignado",
        choices=[("basico", "Básico"), ("intermedio", "Intermedio"), ("avanzado", "Avanzado")],
        validators=[Optional()],
    )
    submit = SubmitField("Guardar estudiante")


class StudentProfileForm(FlaskForm):
    """Valida los datos que el estudiante puede administrar de su perfil."""

    name = StringField("Nombre completo", validators=[DataRequired(), Length(max=150)])
    age = IntegerField("Edad", validators=[DataRequired(), NumberRange(min=5, max=120)])
    school = StringField("Centro educativo", validators=[DataRequired(), Length(max=180)])
    interest_area = StringField("Área de interés", validators=[DataRequired(), Length(max=120)])
    submit = SubmitField("Guardar perfil")


class EducationalContentForm(FlaskForm):
    """Valida recursos educativos y su nivel de dificultad."""

    title = StringField("Título", validators=[DataRequired(), Length(max=180)])
    topic = StringField("Tema", validators=[DataRequired(), Length(max=120)])
    level = SelectField(
        "Nivel",
        choices=[("basico", "Básico"), ("intermedio", "Intermedio"), ("avanzado", "Avanzado")],
        validators=[DataRequired()],
    )
    competency = StringField("Competencia", validators=[DataRequired(), Length(max=180)])
    description = TextAreaField("Descripción", validators=[DataRequired(), Length(max=1200)])
    material_type = SelectField(
        "Tipo de material",
        choices=[("lectura", "Lectura"), ("video", "Video"), ("ejercicio", "Ejercicio"), ("enlace", "Enlace")],
        validators=[Optional()],
    )
    resource_url = StringField("Enlace del recurso", validators=[Optional(), URL(require_tld=False), Length(max=500)])
    status = SelectField(
        "Estado",
        choices=[("activo", "Activo"), ("borrador", "Borrador"), ("archivado", "Archivado")],
        validators=[Optional()],
    )
    submit = SubmitField("Guardar contenido")


class DiagnosticQuestionForm(FlaskForm):
    """Valida preguntas que serán utilizadas en nuevas evaluaciones."""

    topic = StringField("Tema", validators=[DataRequired(), Length(max=120)])
    prompt = TextAreaField("Pregunta", validators=[DataRequired(), Length(max=1200)])
    expected_competency = StringField("Competencia esperada", validators=[DataRequired(), Length(max=180)])
    active = SelectField(
        "Estado",
        choices=[("1", "Activa"), ("0", "Inactiva")],
        validators=[DataRequired()],
    )
    submit = SubmitField("Guardar pregunta")


class DiagnosticEvaluationForm(FlaskForm):
    """Valida la selección del estudiante para una evaluación."""

    student_id = SelectField("Estudiante", coerce=int, validators=[DataRequired()])
    submit = SubmitField("Guardar evaluación")


class UserForm(FlaskForm):
    """Valida los datos mínimos para crear o editar una cuenta del sistema."""

    username = StringField("Usuario", validators=[DataRequired(), Length(min=3, max=80)])
    email = StringField("Correo institucional", validators=[DataRequired(), Email(), Length(max=255)])
    password = PasswordField("Contraseña", validators=[Optional(), Length(min=8, max=128)])
    role = SelectField(
        "Rol",
        choices=[("administrador", "Administrador"), ("docente", "Docente"), ("estudiante", "Estudiante")],
        validators=[DataRequired()],
    )
    active = SelectField(
        "Estado",
        choices=[("1", "Activo"), ("0", "Inactivo")],
        validators=[DataRequired()],
    )
    submit = SubmitField("Guardar usuario")
