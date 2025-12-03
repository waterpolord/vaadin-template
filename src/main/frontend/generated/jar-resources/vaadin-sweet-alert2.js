import { LitElement } from "lit-element";
import Swal from 'sweetalert2'


class VaadinSweetAlert2 extends LitElement {

    static get properties() {
        return {
            alertConfig: Object
        };
    }

    onLoad() {
        if(this.alertConfig !== undefined) {
            Swal.fire(this.alertConfig).then((result)=>{
                window.server.setSweetAlert2Result(JSON.stringify(result));
                if(result.isDismissed || result.isConfirmed) {
                    this.remove();
                }
            });
            Swal.getConfirmButton().addEventListener('click', this.confirmed);
            Swal.getCancelButton().addEventListener('click', this.cancelled);
            Swal.getCloseButton().addEventListener('click', this.closed);
            Swal.getDenyButton().addEventListener('click', this.denied);
        } else {
            console.log("No config found...")
        }
    }

    updateAlert(options) {
        Swal.update(JSON.parse(options));
    }

    firstUpdated(_changedProperties) {
        super.firstUpdated(_changedProperties);
        window.server = this.$server;
    }

    closed() {
        window.server.onClose();
        console.log("closed....");
    }

    confirmed() {
        window.server.onConfirm();
        console.log("confirmed....")
    }

    cancelled() {
        window.server.onCancel();
        console.log("cancelled....")
    }

    denied() {
        window.server.onDeny();
        console.log("denied....")
    }

    createRenderRoot() {
        return this;
    }

}

customElements.get('vaadin-sweet-alert2') || customElements.define('vaadin-sweet-alert2', VaadinSweetAlert2);
