package mk.ukim.finki.emt.model.enums;

/**
 * Created by ristes on 2/23/17.
 */
public enum DeliveryStatus {
  /**
   * Payed invoice. The books should be packed for delivery.
   */
  PENDING_PACKAGE_CREATION,
  /**
   * Books packed. Shipment company called.
   */
  PACKAGE_READY_FOR_SHIPMENT,
  SHIPMENT_IN_PROGRESS,
  CONFIRMED_DELIVERY,
  CLOSED_UNCONFIRMED_DELIVERY
}
