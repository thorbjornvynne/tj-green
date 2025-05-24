
from fastapi import APIRouter

router = APIRouter()

@router.get("/themes")
def get_themes():
    return [
        {
            "id": "forest",
            "name": "Green Forest",
            "primary": "#2E7D32",
            "secondary": "#A5D6A7",
            "background": "https://example.com/backgrounds/forest.jpg"
        },
        {
            "id": "sunset",
            "name": "Sunset Glow",
            "primary": "#FF7043",
            "secondary": "#FFE0B2",
            "background": "https://example.com/backgrounds/sunset.jpg"
        },
        {
            "id": "night",
            "name": "Midnight Blue",
            "primary": "#283593",
            "secondary": "#C5CAE9",
            "background": "https://example.com/backgrounds/night.jpg"
        }
    ]
