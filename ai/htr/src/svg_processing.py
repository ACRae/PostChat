import svgutils.transform as st
import base64

def merge_svgs(svg_path1, svg_path2):
    """
    Merges two svgs together
    Returns data
    """     
    template = st.fromfile(svg_path1)
    second_svg = st.fromfile(svg_path2)
    template.append(second_svg)
    return template

def convert_svg_to_base64(svg):
    """
    Converts SVG file Base64
    """
    svg_data = svg.to_str()
    return base64.b64encode(svg_data, altchars=b'-_').decode('utf-8')