package io.github.rkbalgi.iso4k

/**
 * A wrapper around a IsoField to inspect bits of a Bitmapped field
 */
class IsoBitmapField(private val bmpField: IsoField) {

  /** Returns true if position within the bitmap is defined */
  fun posDefined(pos: Int): Boolean = bmpField.children!!.any { it.position == pos }

  /**
   * Returns a field defined at this position in the Bitmap
   * @return The field definition for the given position
   */
  fun pos(pos: Int): IsoField {
    check(posDefined(pos))
    return bmpField.children!!.first { it.position == pos }
  }
}
