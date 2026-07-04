from flask import Blueprint, render_template
from flask_login import login_required

from app.models import DiagnosticEvaluation, EducationalContent, Student, User


bp = Blueprint("main", __name__)


@bp.get("/")
def index():
    return render_template("main/home.html")


@bp.get("/dashboard")
@login_required
def dashboard():
    metrics = {
        "users": User.query.count(),
        "students": Student.query.count(),
        "contents": EducationalContent.query.count(),
        "evaluations": DiagnosticEvaluation.query.count(),
    }
    return render_template("main/dashboard.html", metrics=metrics)

