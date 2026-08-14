"""
Generate sign textures for Twilight Forest wood types.
Creates 64x32 sign textures for in-world sign rendering.
"""
from PIL import Image
import os
import sys

# Configuration
BASE_DIR = r"s:\Github\Twilightforest-Re26-2\src\main\resources\assets\twilightforest"
TEXTURE_DIR = os.path.join(BASE_DIR, "textures")
BLOCK_DIR = os.path.join(TEXTURE_DIR, "block")
WOOD_DIR = os.path.join(BLOCK_DIR, "wood")

# Output directories
ENTITY_SIGNS_DIR = os.path.join(TEXTURE_DIR, "entity", "signs")
ENTITY_SIGNS_HANGING_DIR = os.path.join(TEXTURE_DIR, "entity", "signs", "hanging")

# Wood type configuration
# name: (planks_texture, stripped_log_texture, display_name)
WOOD_TYPES = [
    ("twilight_oak", "planks_twilight_oak_0.png", "stripped_twilight_oak_log.png", "Twilight Oak"),
    ("canopy", "planks_canopy_0.png", "stripped_canopy_log.png", "Canopy"),
    ("mangrove", "planks_mangrove_0.png", "stripped_mangrove_log.png", "Mangrove"),
    ("dark", "planks_darkwood_0.png", "stripped_dark_log.png", "Darkwood"),
    ("time", "planks_time_0.png", "stripped_time_log.png", "Time"),
    ("transformation", "planks_trans_0.png", "stripped_transformation_log.png", "Transformation"),
    ("mining", "planks_mine_0.png", "stripped_mining_log.png", "Mining"),
    ("sorting", "planks_sort_0.png", "stripped_sorting_log.png", "Sorting"),
]

def load_texture(filename, search_dirs):
    """Load a texture file from one of the search directories."""
    for d in search_dirs:
        path = os.path.join(d, filename)
        if os.path.exists(path):
            return Image.open(path).convert("RGBA")
    raise FileNotFoundError(f"Could not find texture: {filename}")

def create_standing_sign_texture(planks_img, log_img):
    """Create a 64x32 standing sign texture.
    
    Model layout (from StandingSignRenderer.createSignLayer):
    - sign face: texOffs(0,0), box(-12,-14,-1) to (12,-2,1) → 24x12 area at (0,0)
    - stick: texOffs(0,14), box(-1,-2,-1) to (1,12,1) → 2x14 area at (0,14)
    """
    img = Image.new("RGBA", (64, 32), (0, 0, 0, 0))
    
    # Sign face: 24x12 area at (0,0) - use planks texture, tiled
    sign_face = planks_img
    # Tile/crop planks to fill 24x12
    face_texture = Image.new("RGBA", (24, 12), (0, 0, 0, 0))
    for y in range(0, 12, 16):
        for x in range(0, 24, 16):
            # Crop or tile
            crop_w = min(16, 24 - x)
            crop_h = min(16, 12 - y)
            if crop_w > 0 and crop_h > 0:
                cropped = sign_face.crop((0, 0, crop_w, crop_h))
                face_texture.paste(cropped, (x, y))
    
    # Now darken edges slightly for 3D effect
    # Top edge
    for x in range(24):
        for dx in range(2):
            if x + dx < 24:
                px = face_texture.getpixel((x + dx, 0))
                face_texture.putpixel((x + dx, 0), (int(px[0]*0.6), int(px[1]*0.6), int(px[2]*0.6), px[3]))
    # Bottom edge  
    for x in range(24):
        for dx in range(2):
            if x + dx < 24:
                px = face_texture.getpixel((x + dx, 11))
                face_texture.putpixel((x + dx, 11), (int(px[0]*0.6), int(px[1]*0.6), int(px[2]*0.6), px[3]))
    # Left edge
    for y in range(12):
        for dy in range(2):
            if y + dy < 12:
                px = face_texture.getpixel((0, y + dy))
                face_texture.putpixel((0, y + dy), (int(px[0]*0.6), int(px[1]*0.6), int(px[2]*0.6), px[3]))
    # Right edge
    for y in range(12):
        px = face_texture.getpixel((23, y))
        face_texture.putpixel((23, y), (int(px[0]*0.6), int(px[1]*0.6), int(px[2]*0.6), px[3]))
    
    img.paste(face_texture, (0, 0))
    
    # Stick: 2x14 area at (0,14) - use log texture
    stick_texture = Image.new("RGBA", (2, 14), (0, 0, 0, 0))
    for y in range(14):
        for x in range(2):
            src_x = x % log_img.width
            src_y = y % log_img.height
            stick_texture.putpixel((x, y), log_img.getpixel((src_x, src_y)))
    
    # Darken stick edges
    for y in range(14):
        px = stick_texture.getpixel((0, y))
        stick_texture.putpixel((0, y), (int(px[0]*0.7), int(px[1]*0.7), int(px[2]*0.7), px[3]))
    
    img.paste(stick_texture, (0, 14))
    
    return img

def create_hanging_sign_texture(planks_img, log_img):
    """Create a 64x32 hanging sign texture.
    
    Model layout (from HangingSignRenderer.createHangingSignLayer):
    - board: texOffs(0,12), box(-7,0,-1) to (7,10,1) → 14x10 area at (0,12)
    - plank (wall only): texOffs(0,0), box(-8,-6,-2) to (8,-4,2) → 16x2 area at (0,0)
    - chains: various areas
      - chainL1: texOffs(0,6), size 3x6 at (-5,-6,0) rotated
      - chainL2: texOffs(6,6), size 3x6 
      - chainR1: texOffs(0,6), size 3x6
      - chainR2: texOffs(6,6), size 3x6
    - vChains (ceiling_middle): texOffs(14,6), size 12x6
    """
    img = Image.new("RGBA", (64, 32), (0, 0, 0, 0))
    
    # Board: 14x10 area at (0,12)
    board_texture = Image.new("RGBA", (14, 10), (0, 0, 0, 0))
    for y in range(10):
        for x in range(14):
            src_x = x % planks_img.width
            src_y = y % planks_img.height
            board_texture.putpixel((x, y), planks_img.getpixel((src_x, src_y)))
    
    # Darken board edges
    for x in range(14):
        px = board_texture.getpixel((x, 0))
        board_texture.putpixel((x, 0), (int(px[0]*0.6), int(px[1]*0.6), int(px[2]*0.6), px[3]))
        px = board_texture.getpixel((x, 9))
        board_texture.putpixel((x, 9), (int(px[0]*0.6), int(px[1]*0.6), int(px[2]*0.6), px[3]))
    for y in range(10):
        px = board_texture.getpixel((0, y))
        board_texture.putpixel((0, y), (int(px[0]*0.6), int(px[1]*0.6), int(px[2]*0.6), px[3]))
        px = board_texture.getpixel((13, y))
        board_texture.putpixel((13, y), (int(px[0]*0.6), int(px[1]*0.6), int(px[2]*0.6), px[3]))
    
    img.paste(board_texture, (0, 12))
    
    # Plank: 16x2 area at (0,0) - use log texture
    plank_texture = Image.new("RGBA", (16, 2), (0, 0, 0, 0))
    for y in range(2):
        for x in range(16):
            src_x = x % log_img.width
            src_y = y % log_img.height
            plank_texture.putpixel((x, y), log_img.getpixel((src_x, src_y)))
    
    img.paste(plank_texture, (0, 0))
    
    # Chain elements: simple dark gray/brown
    chain_color = (60, 40, 20, 255)
    chain_highlight = (90, 65, 35, 255)
    
    # chainL1: texOffs(0,6), 3x6 - simple chain link pattern
    chain_texture = Image.new("RGBA", (3, 6), (0, 0, 0, 0))
    for y in range(6):
        for x in range(3):
            if y % 2 == 0:
                chain_texture.putpixel((x, y), chain_color)
            else:
                chain_texture.putpixel((x, y), chain_highlight)
    
    img.paste(chain_texture, (0, 6))
    img.paste(chain_texture, (6, 6))
    
    # vChains: texOffs(14,6), 12x6
    vchain_texture = Image.new("RGBA", (12, 6), (0, 0, 0, 0))
    for y in range(6):
        for x in range(12):
            if y % 2 == 0:
                vchain_texture.putpixel((x, y), chain_color)
            else:
                vchain_texture.putpixel((x, y), chain_highlight)
    
    img.paste(vchain_texture, (14, 6))
    
    return img

def main():
    # Create output directories
    os.makedirs(ENTITY_SIGNS_DIR, exist_ok=True)
    os.makedirs(ENTITY_SIGNS_HANGING_DIR, exist_ok=True)
    
    # Search directories for source textures
    search_dirs = [WOOD_DIR, BLOCK_DIR]
    
    for name, planks_file, log_file, display_name in WOOD_TYPES:
        print(f"Processing {display_name} ({name})...")
        
        # Load source textures
        planks_img = load_texture(planks_file, search_dirs)
        log_img = load_texture(log_file, search_dirs)
        
        # Generate standing sign texture
        standing = create_standing_sign_texture(planks_img, log_img)
        standing_path = os.path.join(ENTITY_SIGNS_DIR, f"{name}.png")
        standing.save(standing_path)
        print(f"  Created standing sign: {standing_path}")
        
        # Generate hanging sign texture
        hanging = create_hanging_sign_texture(planks_img, log_img)
        hanging_path = os.path.join(ENTITY_SIGNS_HANGING_DIR, f"{name}.png")
        hanging.save(hanging_path)
        print(f"  Created hanging sign: {hanging_path}")
    
    print("\nAll sign textures generated successfully!")

if __name__ == "__main__":
    main()