import ko from 'knockout';
import {
  register,
  login,
  createCustomer,
  createProduct,
  createQuote,
  priceQuote,
  confirmQuote,
  createClaim,
  assessClaim,
  closeClaim,
  createPayment,
  uploadDocument,
  sendTestEmail,
  getPolicies
} from '../services/apiService.js';

function WorkflowViewModel() {
  this.log = ko.observable('');

  this.run = async () => {
    const steps = [];
    const creds = { user: 'alice', pw: 'secret' };
    try {
      steps.push('register user');
      await register({ username: creds.user, password: creds.pw, role: 'USER' });

      steps.push('login');
      await login({ username: creds.user, password: creds.pw });

      steps.push('create customer');
      const customer = await createCustomer(creds, {
        firstName: 'John',
        lastName: 'Doe',
        email: 'john@example.com',
        phone: '1234567890',
        dob: '1990-01-01'
      });

      steps.push('create product');
      const product = await createProduct(creds, {
        name: 'Term Life',
        code: 'TERM01',
        description: 'Life cover',
        baseRatePer1000: 5,
        minSumAssured: 50000,
        maxSumAssured: 500000,
        minTermMonths: 12,
        maxTermMonths: 240,
        active: true
      });

      steps.push('create quote');
      const quote = await createQuote(creds, {
        customerId: customer.id,
        productId: product.id,
        sumAssured: 100000,
        termMonths: 120
      });

      steps.push('price quote');
      await priceQuote(creds, quote.id);

      steps.push('confirm quote');
      const policy = await confirmQuote(creds, quote.id);

      steps.push('make payment');
      await createPayment({
        targetType: 'POLICY',
        targetId: policy.id,
        amount: 1000,
        method: 'CARD'
      });

      steps.push('upload document');
      const blob = new Blob(['demo'], { type: 'text/plain' });
      await uploadDocument({ ownerId: policy.id, ownerType: 'POLICY', label: 'ID Proof' }, blob);

      steps.push('create claim');
      const claim = await createClaim(creds, {
        policyId: policy.id,
        lossDate: '2024-01-10',
        description: 'Accident'
      });

      steps.push('assess claim');
      await assessClaim(creds, claim.id, {
        decision: 'APPROVE',
        approvedAmount: 5000,
        reason: 'Valid docs'
      });

      steps.push('close claim');
      await closeClaim(creds, claim.id);

      steps.push('send notification');
      await sendTestEmail({
        eventId: 'evt-1',
        template: 'welcome',
        to: ['user@example.com'],
        cc: [],
        subject: 'Hello',
        model: { name: 'User' },
        metadata: { origin: 'test' },
        sendAfter: '2024-06-01T10:00:00Z'
      });

      const policies = await getPolicies(creds, `customer_id=${customer.id}`);
      steps.push(`workflow complete: ${policies.length} policy(s)`);

      this.log(steps.join('\n'));
    } catch (e) {
      steps.push(`error: ${e.message}`);
      this.log(steps.join('\n'));
    }
  };
}

export default WorkflowViewModel;

