from __future__ import annotations

import atexit
import os
import signal

from app import create_app, seed_database
from ai_provider import AIProviderError, ChatProvider
from app.extensions import db


def install_shutdown_handlers(provider: ChatProvider) -> None:
    def shutdown_handler(signum, _frame):
        provider.shutdown()
        raise SystemExit(128 + signum)

    signal.signal(signal.SIGTERM, shutdown_handler)
    signal.signal(signal.SIGINT, shutdown_handler)


app = create_app()


if __name__ == "__main__":
    ai_provider: ChatProvider = app.extensions["ai_provider"]
    with app.app_context():
        db.create_all()
        seed_database()
    atexit.register(ai_provider.shutdown)
    install_shutdown_handlers(ai_provider)
    try:
        ai_provider.ensure_ready()
    except AIProviderError as error:
        print(f"Advertencia: {error}")

    try:
        app.run(
            host=os.getenv("APP_HOST", "127.0.0.1"),
            port=int(os.getenv("APP_PORT", "5050")),
            debug=os.getenv("FLASK_DEBUG", "0") == "1",
            use_reloader=False,
            threaded=True,
        )
    finally:
        ai_provider.shutdown()
