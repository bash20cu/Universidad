from flask_wtf import FlaskForm
from wtforms import IntegerField, PasswordField, SelectField, StringField, SubmitField, TextAreaField
from wtforms.validators import DataRequired, Email, Length, NumberRange, Regexp


class LoginForm(FlaskForm):
    username = StringField("Usuario", validators=[DataRequired(), Length(max=80)])
    password = PasswordField("Contraseña", validators=[DataRequired(), Length(max=128)])
    submit = SubmitField("Continuar")


class TwoFactorForm(FlaskForm):
    code = StringField(
        "Código de verificación",
        validators=[DataRequired(), Regexp(r"^\d{6}$", message="Ingresa 6 dígitos.")],
    )
    submit = SubmitField("Verificar")


class EmailForm(FlaskForm):
    email = StringField("Correo", validators=[DataRequired(), Email(), Length(max=255)])


class StudentForm(FlaskForm):
    name = StringField("Nombre completo", validators=[DataRequired(), Length(max=150)])
    age = IntegerField("Edad", validators=[DataRequired(), NumberRange(min=5, max=120)])
    school = StringField("Centro educativo", validators=[DataRequired(), Length(max=180)])
    interest_area = StringField("Área de interés", validators=[DataRequired(), Length(max=120)])
    assigned_level = SelectField(
        "Nivel asignado",
        choices=[("basico", "Básico"), ("intermedio", "Intermedio"), ("avanzado", "Avanzado")],
        validators=[DataRequired()],
    )
    submit = SubmitField("Guardar estudiante")


class EducationalContentForm(FlaskForm):
    title = StringField("Título", validators=[DataRequired(), Length(max=180)])
    topic = StringField("Tema", validators=[DataRequired(), Length(max=120)])
    level = SelectField(
        "Nivel",
        choices=[("basico", "Básico"), ("intermedio", "Intermedio"), ("avanzado", "Avanzado")],
        validators=[DataRequired()],
    )
    competency = StringField("Competencia", validators=[DataRequired(), Length(max=180)])
    description = TextAreaField("Descripción", validators=[DataRequired(), Length(max=1200)])
    submit = SubmitField("Guardar contenido")


class DiagnosticEvaluationForm(FlaskForm):
    student_id = SelectField("Estudiante", coerce=int, validators=[DataRequired()])
    submit = SubmitField("Guardar evaluación")
